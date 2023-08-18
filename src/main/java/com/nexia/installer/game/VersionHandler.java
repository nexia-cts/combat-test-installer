package com.nexia.installer.game;
import java.util.ArrayList;
import java.util.List;

public class VersionHandler {
    public static List<GameVersion> versions = new ArrayList<>();

    public static GameVersion CombatTest8c = new GameVersion("Combat Test 8c", "1.16_combat-6", new Download("9879e6ba375eda28ab8fc3b83ca9febe5c11d54b", (long) 7.463, "https://www.rizecookey.net/dl/file/combat-test-patched/1_16_combat-6.zip"));

    public static GameVersion CombatTest8b = new GameVersion("Combat Test 8b", "1.16_combat-5", new Download("05b81ee7c117524580c477900277c316d3436e94", (long) 7.044, "https://launcher.mojang.com/experiments/combat/9b2b984d635d373564b50803807225c75d7fd447/1_16_combat-5.zip"));

    public static GameVersion CombatTest8 = new GameVersion("Combat Test 8", "1.16_combat-4", new Download("d8d9dd198d0a46435c3d1eac75633a387392d793", (long) 7.046, "https://cdn.discordapp.com/attachments/369990015096455168/947864881028272198/1_16_combat-4.zip"));

    public static class GameVersion {
        String version;
        String codeName;
        Download download;

        public GameVersion(String version, String codeName, Download download) {
            this.version = version;
            this.codeName = codeName;
            this.download = download;

            VersionHandler.versions.add(this);
        }

        public String getVersion() {
            return version;
        }

        public String getCodeName() {
            return codeName;
        }

        public Download getDownload() {
            return download;
        }
    }

    public static class Download {
        public final String sha1;
        public final long size;
        public final String url;

        public Download(String sha1, long size, String url) {
            this.sha1 = sha1;
            this.size = size;
            this.url = url;
        }
    }

    public static GameVersion identifyGameVersion(String version) {
        if(version.trim().isEmpty()) return null;
        for(GameVersion gameVersion : VersionHandler.versions) {
            if(version.equalsIgnoreCase(gameVersion.getCodeName()) || version.equalsIgnoreCase(gameVersion.getVersion())) return gameVersion;
        }
        return null;
    }
}
