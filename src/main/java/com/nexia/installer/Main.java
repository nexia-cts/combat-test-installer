package com.nexia.installer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Main {

    public static OS os = null;

    public static Image icon;

    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("lang/installer", Locale.getDefault(), new ResourceBundle.Control() {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            final String bundleName = toBundleName(baseName, locale);
            final String resourceName = toResourceName(bundleName, "properties");

            try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                if (stream != null) {
                    try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                        return new PropertyResourceBundle(reader);
                    }
                }
            }

            return super.newBundle(baseName, locale, format, loader, reload);
        }
    });

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        os = OS.LINUX;

        if (System.getProperty("os.name").contains("win")) {
            os = OS.WINDOWS;
            System.setProperty("javax.net.ssl.trustStoreType", "WINDOWS-ROOT");
        } else if (System.getProperty("os.name").contains("mac")) {
            os = OS.MAC;
        }

        icon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png"));
        InstallerGUI.load();
    }

    public enum OS {
        WINDOWS,
        LINUX,
        MAC
    }
}