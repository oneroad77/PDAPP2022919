package com.example.pdapp2022919;

import android.os.Build;

import com.example.pdapp2022919.Recode.RecodeData;
import com.example.pdapp2022919.net.Client;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {

    public enum FileType {
        GAME("game"), SHORT_LINE("shortLine"), PROFILE("profile");

        final String displayName;

        FileType(String displayName) {
            this.displayName = displayName;
        }

    }

    private static String fileDir;
    private static String gameFilePrefix;
    private static String shortLineFilePrefix;
    private static String gameFolder;
    private static String shortLineFolder;

    public static void setFileDir(String path) {
        fileDir = path;
    }

    public static void setTimestamp(FileType type) {
        long timestamp = System.currentTimeMillis();
        switch (type) {
            case GAME:
                gameFilePrefix = String.join("_", new String[] {
                        Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                gameFolder = String.join("/", new String[] {
                        fileDir, Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                checkFolder(gameFolder);
                break;
            case SHORT_LINE:
                shortLineFilePrefix = String.join("_", new String[] {
                        Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                shortLineFolder = String.join("/", new String[] {
                        fileDir, Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                checkFolder(shortLineFolder);
                break;
            case PROFILE:
                break;
        }
    }

    public static File getPreTestFile() {
        return new File(gameFolder, gameFilePrefix + "_preTest.gp3");
    }

    public static File getPostTestFile() {
        return new File(gameFolder, gameFilePrefix + "_postTest.gp3");
    }

    public static void writeHistoryFile(RecodeData data) {
        File history = new File(gameFolder, gameFilePrefix + "_history.json");
        String gson = new Gson().toJson(data);
        try {
            FileOutputStream os = new FileOutputStream(history);
            os.write(gson.getBytes());
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public static File getShortLineFile() {
        return new File(shortLineFolder, shortLineFilePrefix + "_shortLine.gp3");
    }

    private static void checkFolder(String folder) {
        File dir = new File(folder);
        if (dir.exists()) return;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.createDirectories(Paths.get(folder));
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

}
