package com.nexia.installer.util;

import com.nexia.installer.Main;
import com.nexia.installer.game.VersionHandler;

import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

public class InstallerUtils {
    public static Path findDefaultInstallDir() {
        Path dir;

        if (Main.os.equals(Main.OS.WINDOWS) && System.getenv("APPDATA") != null) {
            dir = Paths.get(System.getenv("APPDATA")).resolve(".minecraft");
        } else {
            String home = System.getProperty("user.home", ".");
            Path homeDir = Paths.get(home);

            if (Main.os.equals(Main.OS.MAC)) {
                dir = homeDir.resolve("Library").resolve("Application Support").resolve("minecraft");
            } else {
                dir = homeDir.resolve(".minecraft");

                if (Main.os.equals(Main.OS.LINUX) && !Files.exists(dir)) {
                    // https://github.com/flathub/com.mojang.Minecraft
                    final Path flatpack = homeDir.resolve(".var").resolve("app").resolve("com.mojang.Minecraft").resolve(".minecraft");

                    if (Files.exists(flatpack)) {
                        dir = flatpack;
                    }
                }
            }
        }

        return dir.toAbsolutePath().normalize();
    }

    public static void install(Path mcDir, VersionHandler.GameVersion gameVersion) {
        if(mcDir == null || gameVersion == null) return;


        new Thread(() -> {
           try {
               final ProfileInstaller profileInstaller = new ProfileInstaller(mcDir);
               ProfileInstaller.LauncherType launcherType = null;

               if(InstallerHelper.createProfile.isSelected()) {
                   List<ProfileInstaller.LauncherType> types = profileInstaller.getInstalledLauncherTypes();

                   if (types.isEmpty()) {
                       throw new RuntimeException(Main.BUNDLE.getString("installer.exception.no.launcher.profile"));
                   } else if (types.size() == 1) {
                       launcherType = types.get(0);
                   } else {
                       launcherType = showLauncherTypeSelection();

                       if (launcherType == null) {
                           return;
                       }
                   }
               }

               String alternativeCodeName = gameVersion.getCodeName().replaceAll("\\.", "_");

               Path versionsDir = mcDir.resolve("versions");
               Path profileDir = versionsDir.resolve(alternativeCodeName);
               Path profileJson = profileDir.resolve(alternativeCodeName + ".json");

               if (!Files.exists(profileDir)) {
                   Files.createDirectory(profileDir);
                   Files.createFile(profileJson);
               }

               File zipFile = new File(versionsDir + "/" + gameVersion.getCodeName() + ".zip");

               Utils.downloadFile(URI.create(gameVersion.getDownload().url).toURL(), zipFile.toPath());
               Utils.extractZip(zipFile.toPath(), versionsDir);

               zipFile.delete();

               if (InstallerHelper.createProfile.isSelected()) {
                   if (launcherType == null) {
                       throw new RuntimeException(Main.BUNDLE.getString("installer.exception.no.launcher.profile"));
                   }

                   profileInstaller.setupProfile(gameVersion.getCodeName(), gameVersion.getVersion(), launcherType);
               }
           } catch (Exception e) {
               e.printStackTrace();
           } finally {
               InstallerHelper.buttonInstall.setEnabled(true);
           }
        }).start();
    }

    private static ProfileInstaller.LauncherType showLauncherTypeSelection() {
        Object[] options = { Main.BUNDLE.getString("installer.prompt.launcher.type.xbox"), Main.BUNDLE.getString("installer.prompt.launcher.type.win32")};

        int result = JOptionPane.showOptionDialog(null,
                Main.BUNDLE.getString("installer.prompt.launcher.type.body"),
                Main.BUNDLE.getString("installer.title"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (result == JOptionPane.CLOSED_OPTION) {
            return null;
        }

        return result == JOptionPane.YES_OPTION ? ProfileInstaller.LauncherType.MICROSOFT_STORE : ProfileInstaller.LauncherType.WIN32;
    }
}
