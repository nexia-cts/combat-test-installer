package com.nexia.installer.util.fabric;

import com.nexia.installer.InstallerGUI;
import com.nexia.installer.Main;
import com.nexia.installer.util.InstallerHelper;
import com.nexia.installer.util.InstallerUtils;
import com.nexia.installer.util.ProfileInstaller;
import com.nexia.installer.util.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

public class FabricInstallerHelper extends InstallerHelper {
    public static JButton buttonInstall;

    public static JButton buttonFabric;
    public JComboBox<String> gameVersionComboBox;
    public JTextField installLocation;
    public JButton selectFolderButton;

    public static JCheckBox createProfile;

    @Override
    public JPanel setPanel(InstallerGUI gui) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(4, 4, 4, 4));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 4, 6, 4);
        c.gridx = c.gridy = 0;

        addRow(panel, c, "installer.prompt.game.version",
                gameVersionComboBox = new JComboBox<>(),
                createSpacer()
        );

        for(FabricVersionHandler.GameVersion version : FabricVersionHandler.versions) {
            gameVersionComboBox.addItem(version.getVersion());
        }

        addRow(panel, c, "installer.prompt.select.location",
                installLocation = new JTextField(20),
                selectFolderButton = new JButton());
        selectFolderButton.setText("...");
        selectFolderButton.setPreferredSize(new Dimension(installLocation.getPreferredSize().height, installLocation.getPreferredSize().height));
        selectFolderButton.addActionListener(e -> InstallerGUI.selectInstallLocation(() -> installLocation.getText(), s -> installLocation.setText(s)));

        addRow(panel, c, null,
                createProfile = new JCheckBox(Main.BUNDLE.getString("installer.option.create.profile"), true));

        installLocation.setText(InstallerUtils.findDefaultInstallDir().toString());

        addLastRow(panel, c,
                buttonInstall = new JButton(Main.BUNDLE.getString("installer.button.install")));
        buttonInstall.addActionListener(e -> {
            buttonInstall.setEnabled(false);
            try {
                launch();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        addLastRow(panel, c,
                buttonFabric = new JButton(Main.BUNDLE.getString("installer.button.fabric")));
        buttonFabric.addActionListener(e -> {
            try {
                Process process = Runtime.getRuntime().exec("java -jar cache/" + getJarFile().getName());
                while(process.isAlive()) {
                    buttonFabric.setEnabled(false);
                }
                buttonFabric.setEnabled(true);
            } catch (IOException ex) {
                try {
                    getJarFile().delete();
                } catch (IOException exc) {
                    // what the fuck
                    exc.printStackTrace();
                }

                buttonFabric.setEnabled(false);
            }
        });

        return panel;
    }

    @Override
    public void launch() throws IOException {

        String stringGameVersion = (String) gameVersionComboBox.getSelectedItem();
        FabricVersionHandler.GameVersion gameVersion = FabricVersionHandler.identifyGameVersion(stringGameVersion);
        if(gameVersion == null) return;


        Path mcPath = Paths.get(installLocation.getText());

        if (!Files.exists(mcPath)) {
            throw new RuntimeException(Main.BUNDLE.getString("installer.exception.no.launcher.directory"));
        }

        final ProfileInstaller profileInstaller = new ProfileInstaller(mcPath);
        ProfileInstaller.LauncherType launcherType;
        String launcherThing = "";

        // can you tell i have no fucking idea what im doing?
        if(InstallerHelper.createProfile.isSelected()) {
            List<ProfileInstaller.LauncherType> types = profileInstaller.getInstalledLauncherTypes();

            if (types.isEmpty()) {
                throw new RuntimeException(Main.BUNDLE.getString("installer.exception.no.launcher.profile"));
            } else {
                launcherType = InstallerUtils.showLauncherTypeSelection();

                if (launcherType == null) {
                    return;
                }

                launcherThing = "-launcher " + launcherType.name;
            }
        }

        System.out.println("Installing Fabric " + gameVersion.getVersion() + " (" + gameVersion.getCodeName() + ")");


        // help - Opens this menu
        //client -dir <install dir> -mcversion <minecraft version, default latest> -loader <loader version, default latest> -launcher [win32, microsoft_store]
        //server -dir <install dir, default current dir> -mcversion <minecraft version, default latest> -loader <loader version, default latest> -downloadMinecraft

        String[] cmd2 = new String[]{"java", "-jar", "cache/" + getJarFile().getName(), "client", "-dir" + "\"" + mcPath.toAbsolutePath() + "\"", "-mcversion", gameVersion.codeName, launcherThing};


        Process process = Runtime.getRuntime().exec(cmd2);
        // instead of checking if is done, ykyk
        // lets just show them its already done!
        // who would notice!!!

        this.showDone(gameVersion);
        buttonInstall.setEnabled(true);
    }


    private File getJarFile() throws IOException {
        Path currentDir = new File("").toPath();
        Path cacheDir = currentDir.resolve("cache");

        String fileName = "fabric-installer-" + getFabricVersion() + ".jar";
        URL url = new URL("https://github.com/rizecookey/fabric-installer/releases/latest/download/" + fileName);

        if(!Files.exists(cacheDir)) {
            Files.createDirectories(cacheDir);
            Utils.downloadFile(url, cacheDir.resolve(fileName));
            return new File(cacheDir.toFile(), fileName);

        }

        for(File file : cacheDir.toFile().listFiles()) {
            if(file.getName().equals(fileName)) {
                return file;
            } else {
                file.delete();
                Files.createDirectory(cacheDir);
                Utils.downloadFile(url, cacheDir.resolve(fileName));
                return new File(cacheDir.toFile(), fileName);
            }
        }

        return null;
    }

    private String getFabricVersion() {
        return "0.11.1";
    }

    private void showDone(FabricVersionHandler.GameVersion gameVersion) {
        Object[] options = {"OK", "Install Vanilla"};
        int result = JOptionPane.showOptionDialog(null,
                MessageFormat.format(Main.BUNDLE.getString("installer.prompt.install.done.fabric"), gameVersion.getVersion()),
                Main.BUNDLE.getString("installer.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if(result == JOptionPane.NO_OPTION) InstallerGUI.instance.pane.setSelectedComponent(InstallerGUI.instance.vanilla);
    }
}

