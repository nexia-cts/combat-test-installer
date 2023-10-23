package com.nexia.installer.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    public static final DateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public static void extractZip(Path file, Path path) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(Paths.get(file.toString())));
        ZipEntry entry = zipIn.getNextEntry();
        String filePath;
        // iterates over entries in the zip file
        while (entry != null) {
            filePath = path + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(filePath)));
                byte[] bytesIn = new byte[8096];
                int read;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    public static String readString(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
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

    public static String getProfileIcon() {
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream("icon.png")) {
            byte[] ret = new byte[4096];
            int offset = 0;
            int len;

            while ((len = is.read(ret, offset, ret.length - offset)) != -1) {
                offset += len;
                if (offset == ret.length) ret = Arrays.copyOf(ret, ret.length * 2);
            }

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(Arrays.copyOf(ret, offset));
        } catch (IOException e) {
            return "furnace"; // Fallback to furnace icon if we cant load Nexia icon.
        }
    }
}
