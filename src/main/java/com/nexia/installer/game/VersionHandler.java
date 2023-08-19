package com.nexia.installer.game;

import java.util.ArrayList;
import java.util.List;

public class VersionHandler {
    public static List<GameVersion> versions = new ArrayList<>();

    public static GameVersion CombatTest8c = new GameVersion("Combat Test 8c", "1.16_combat-6", new Download("f76157734ee0611fbef7d636156f20fa67c34514", 7463, "https://www.rizecookey.net/dl/file/combat-test-patched/1_16_combat-6.zip"));

    public static GameVersion CombatTest8b = new GameVersion("Combat Test 8b", "1.16_combat-5", new Download("9b2b984d635d373564b50803807225c75d7fd447", 7044, "https://launcher.mojang.com/experiments/combat/9b2b984d635d373564b50803807225c75d7fd447/1_16_combat-5.zip"));

    public static GameVersion CombatTest8 = new GameVersion("Combat Test 8", "1.16_combat-4", new Download("b4306b421183bd084b2831bd8d33a5db05ae9f9c", 7046, "https://cdn.discordapp.com/attachments/369990015096455168/947864881028272198/1_16_combat-4.zip"));

    public static GameVersion CombatTest7c = new GameVersion("Combat Test 7c", "1.16_combat-3", new Download("2557b99d95588505e988886220779087d7d6b1e9", 7049, "https://piston-data.mojang.com/experiments/combat/2557b99d95588505e988886220779087d7d6b1e9/1_16_combat-3.zip"));

    public static GameVersion CombatTest7b = new GameVersion("Combat Test 7b", "1.16_combat-2", new Download("43266ea8f2c20601d9fb264d5aa85df8052abc9e", 7051, "https://archive.org/download/Combat_Test_7ab/1_16_combat-2.zip"));
    public static GameVersion CombatTest7 = new GameVersion("Combat Test 7", "1.16_combat-1", new Download("47bb5be6cb3ba215539ee97dfae66724c73c3dd5", 7045, "https://archive.org/download/Combat_Test_7ab/1_16_combat-1.zip"));

    public static GameVersion CombatTest6 = new GameVersion("Combat Test 6", "1.16_combat-0", new Download("5a8ceec8681ed96ab6ecb9607fb5d19c8a755559", 7049, "https://piston-data.mojang.com/experiments/combat/5a8ceec8681ed96ab6ecb9607fb5d19c8a755559/1_16_combat-0.zip"));
    public static GameVersion CombatTest5 = new GameVersion("Combat Test 5", "1.15_combat-6", new Download("52263d42a626b40c947e523128f7a195ec5af76a", 7061, "https://piston-data.mojang.com/experiments/combat/52263d42a626b40c947e523128f7a195ec5af76a/1_15_combat-6.zip"));
    public static GameVersion CombatTest4 = new GameVersion("Combat Test 4", "1.15_combat-1", new Download("ac11ea96f3bb2fa2b9b76ab1d20cacb1b1f7ef60", 7059, "https://piston-data.mojang.com/experiments/combat/ac11ea96f3bb2fa2b9b76ab1d20cacb1b1f7ef60/1_15_combat-1.zip"));
    public static GameVersion CombatTest3 = new GameVersion("Combat Test 3", "1.14_combat-3", new Download("0f209c9c84b81c7d4c88b4632155b9ae550beb89", 6433, "https://piston-data.mojang.com/experiments/combat/0f209c9c84b81c7d4c88b4632155b9ae550beb89/1_14_combat-3.zip"));
    public static GameVersion CombatTest2 = new GameVersion("Combat Test 2", "1.14_combat-0", new Download("d164bb6ecc5fca9ac02878c85f11befae61ac1ca", 6287, "https://piston-data.mojang.com/experiments/combat/d164bb6ecc5fca9ac02878c85f11befae61ac1ca/1_14_combat-0.zip"));
    public static GameVersion CombatTest1 = new GameVersion("1.14.3 - Combat Test", "1.14_combat-212796", new Download("610f5c9874ba8926d5ae1bcce647e5f0e6e7c889",4843, "https://piston-data.mojang.com/experiments/combat/610f5c9874ba8926d5ae1bcce647e5f0e6e7c889/1_14_combat-212796.zip"));

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
