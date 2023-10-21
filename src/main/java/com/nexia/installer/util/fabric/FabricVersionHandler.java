package com.nexia.installer.util.fabric;

import java.util.ArrayList;
import java.util.List;

public class FabricVersionHandler {

    public static List<GameVersion> versions = new ArrayList<>();

    public static GameVersion CombatTest8c = new GameVersion("Combat Test 8c", "1.16_combat-6");
    public static GameVersion CombatTest7c = new GameVersion("Combat Test 7c", "1.16_combat-3");
    public static GameVersion CombatTest1 = new GameVersion("1.14.3 - Combat Test", "1.14_combat-212796");

    public static class GameVersion {
        String version;
        String codeName;

        public GameVersion(String version, String codeName) {
            this.version = version;
            this.codeName = codeName;

            FabricVersionHandler.versions.add(this);
        }

        public String getVersion() {
            return version;
        }

        public String getCodeName() {
            return codeName;
        }

    }

    public static GameVersion identifyGameVersion(String version) {
        if(version.trim().isEmpty()) return null;
        for(GameVersion gameVersion : FabricVersionHandler.versions) {
            if(version.equalsIgnoreCase(gameVersion.getCodeName()) || version.equalsIgnoreCase(gameVersion.getVersion())) return gameVersion;
        }
        return null;
    }
}
