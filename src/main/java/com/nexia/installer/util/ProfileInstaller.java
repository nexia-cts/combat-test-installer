/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nexia.installer.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import mjson.Json;

public class ProfileInstaller {
    private final Path mcDir;

    public ProfileInstaller(Path mcDir) {
        this.mcDir = mcDir;
    }

    public List<LauncherType> getInstalledLauncherTypes() {
        return Arrays.stream(LauncherType.values())
                .filter(launcherType -> Files.exists(mcDir.resolve(launcherType.profileJsonName)))
                .collect(Collectors.toList());
    }

    public void setupProfile(String name, String gameVersion, LauncherType launcherType) throws IOException {
        Path launcherProfiles = mcDir.resolve(launcherType.profileJsonName);

        if (!Files.exists(launcherProfiles)) {
            throw new FileNotFoundException("Could not find " + launcherType.profileJsonName);
        }

        System.out.println("Creating profile");

        Json jsonObject = Json.read(Utils.readString(launcherProfiles));

        Json profiles = jsonObject.at("profiles");

        if (profiles == null) {
            profiles = Json.object();
            jsonObject.set("profiles", profiles);
        }

        Json profile = profiles.at(name);

        if (profile == null) {
            profile = createProfile(gameVersion);
            profiles.set(name, profile);
        }

        profile.set("lastVersionId", name);

        Utils.writeToFile(launcherProfiles, jsonObject.toString());
    }

    private static Json createProfile(String name) {
        Json jsonObject = Json.object();
        jsonObject.set("name", name);
        jsonObject.set("type", "custom");
        jsonObject.set("created", Utils.ISO_8601.format(new Date()));
        jsonObject.set("lastUsed", Utils.ISO_8601.format(new Date()));
        jsonObject.set("icon", Utils.getProfileIcon());
        return jsonObject;
    }

    public enum LauncherType {
        WIN32("win32", "launcher_profiles.json"),
        MICROSOFT_STORE("microsoft_store", "launcher_profiles_microsoft_store.json");

        public final String profileJsonName;

        public final String name;

        LauncherType(String name, String profileJsonName) {
            this.name = name;
            this.profileJsonName = profileJsonName;
        }

    }
}
