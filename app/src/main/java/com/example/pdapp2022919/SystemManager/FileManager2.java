package com.example.pdapp2022919.SystemManager;

import android.os.Build;

import com.example.pdapp2022919.HealthManager.History.HistoryItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class FileManager2 {

    private static String fileDir;

    public static void setFileDir(String path) {
        fileDir = path;
    }

    public static String getFolder(FileType type, HistoryItem item) {
        String folder = fileDir + "/" + type.fileType.toLowerCase(Locale.ROOT) + "/"  + item.getTimeText();
        createFolder(folder);
        return folder;
    }

    public static String getWavPath(FileType type, HistoryItem item) {
        return getFolder(type, item) + "/" + type.fileName + ".wav";
    }

    public static String getTempRaw() {
        String folder = fileDir + "/temp";
        createFolder(folder);
        return folder + "/temp_record.raw";
    }

    public static boolean deleteFile(File[] files) {
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) return false;
            }
        }
        return true;
    }

    private static void createFolder(String path) {
        File dir = new File(path);
        if (dir.exists()) return;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.createDirectories(Paths.get(path));
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

}
