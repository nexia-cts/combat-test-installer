package com.nexia.installer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Main {

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
    public static void main(String[] args) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            System.setProperty("javax.net.ssl.trustStoreType", "WINDOWS-ROOT");
        }

        new InstallerGUI();
    }
}