package com.nexia.installer.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

public class WebUtils {
    public static String readString(URL url) throws IOException {
        try (InputStream is = openUrl(url)) {
            return readString(is);
        }
    }

    public static String readString(InputStream is) throws IOException {
        byte[] data = new byte[Math.max(1000, is.available())];
        int offset = 0;
        int len;

        while ((len = is.read(data, offset, data.length - offset)) >= 0) {
            offset += len;

            if (offset == data.length) {
                int next = is.read();
                if (next < 0) break;

                data = Arrays.copyOf(data, data.length * 2);
                data[offset++] = (byte) next;
            }
        }

        return new String(data, 0, offset, StandardCharsets.UTF_8);
    }

    public static void writeToFile(Path path, String string) throws IOException {
        Files.write(path, string.getBytes(StandardCharsets.UTF_8));
    }

    public static void downloadFile(URL url, Path path) throws IOException {
        try (InputStream in = openUrl(url)) {
            Files.createDirectories(path.getParent());
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable t) {
            try {
                Files.deleteIfExists(path);
            } catch (Throwable t2) {
                t.addSuppressed(t2);
            }

            throw t;
        }
    }

    private static final int HTTP_TIMEOUT_MS = 8000;

    private static InputStream openUrl(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(HTTP_TIMEOUT_MS);
        conn.setReadTimeout(HTTP_TIMEOUT_MS);
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) throw new IOException("HTTP request to "+url+" failed: "+responseCode);

        return conn.getInputStream();
    }

    public static String sha1String(Path path) throws IOException {
        return bytesToHex(sha1(path));
    }

    public static byte[] sha1(Path path) throws IOException {
        MessageDigest digest = sha1Digest();

        try (InputStream is = Files.newInputStream(path)) {
            byte[] buffer = new byte[64 * 1024];
            int len;

            while ((len = is.read(buffer)) >= 0) {
                digest.update(buffer, 0, len);
            }
        }

        return digest.digest();
    }

    private static MessageDigest sha1Digest() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Something has gone really wrong", e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder output = new StringBuilder();

        for (byte b : bytes) {
            output.append(String.format(Locale.ENGLISH, "%02x", b));
        }

        return output.toString();
    }
}