package com.nexia.installer.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InstallerUtils {

    public static void installTest() throws IOException {
        Path path = Paths.get(System.getenv("APPDATA")).resolve(".minecraft").resolve("testing.txt");

        WebUtils.writeToFile(path, "testing!");
    }
}
