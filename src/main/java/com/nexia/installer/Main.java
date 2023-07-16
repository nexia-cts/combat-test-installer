package com.nexia.installer;

public class Main {
    public static void main(String[] args) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            System.setProperty("javax.net.ssl.trustStoreType", "WINDOWS-ROOT");
        }

        new InstallerGUI();
    }
}