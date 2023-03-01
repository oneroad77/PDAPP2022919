package com.example.pdapp2022919;

import android.os.Build;
import android.os.FileUtils;

import com.example.pdapp2022919.Profile.ProfileData;
import com.example.pdapp2022919.Recode.RecodeData;
import com.example.pdapp2022919.net.Client;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileManager {

    public enum FileType {
        GAME("game"), SHORT_LINE("shortLine"), PROFILE("profile");

        final String displayName;

        FileType(String displayName) {
            this.displayName = displayName;
        }

    }

    public static class HistoryData {
        public final Date date;
        public final FileType type;

        private HistoryData(FileType type, Date date) {
            this.type = type;
            this.date = date;
        }
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final int READ_BUFFER_LIMIT = 1024;

    private static String fileDir;
    private static String gameFilePrefix;
    private static String profilePrefix;
    private static String shortLineFilePrefix;
    private static String gameFolder;
    private static String shortLineFolder;
    private static String profileFolder;

    public static void setFileDir(String path) {
        fileDir = path;
    }

    public static void setTimestamp(FileType type) {
        long timestamp = System.currentTimeMillis();
        switch (type) {
            case GAME:
                gameFilePrefix = String.join("_", new String[]{
                        Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                gameFolder = String.join("/", new String[]{
                        fileDir, Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                checkFolder(gameFolder);
                break;
            case SHORT_LINE:
                shortLineFilePrefix = String.join("_", new String[]{
                        Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                shortLineFolder = String.join("/", new String[]{
                        fileDir, Client.getUuid().toString(), type.displayName, Long.toString(timestamp)
                });
                checkFolder(shortLineFolder);
                break;
            case PROFILE:
                profilePrefix = String.join("_", new String[]{
                        Client.getUuid().toString(), type.displayName
                });
                profileFolder = String.join("/", new String[]{
                        fileDir, Client.getUuid().toString(), type.displayName
                });
                checkFolder(profileFolder);
                break;
        }
    }

    public static File getPreTestFile() {
        return new File(gameFolder, gameFilePrefix + "_preTest.gp3");
    }

    public static File getPostTestFile() {
        return new File(gameFolder, gameFilePrefix + "_postTest.gp3");
    }

    public static void writeProfile(ProfileData data) {
        File profile = new File(profileFolder, profilePrefix + "_profile.json");
        System.out.println(profile.getAbsolutePath());
        String gson = new Gson().toJson(data);
        try {
            FileOutputStream os = new FileOutputStream(profile);
            os.write(gson.getBytes());
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public static ProfileData readProfile() {
        File profile = new File(profileFolder, profilePrefix + "_profile.json");
        System.out.println(profile.getAbsolutePath());
        if (!profile.exists()) {
            ProfileData file = new ProfileData();
            file.patient_name = "one路";
            file.patient_number = "123456789";
            return file;
        }
        StringBuilder builder = new StringBuilder();
        try {
            FileInputStream is = new FileInputStream(profile);
            int ava = is.available();// 確認有多少東西可以讀
            while (ava > 0) {//如果讀的東西大於零
                byte[] bytes = new byte[Math.min(ava, READ_BUFFER_LIMIT)];//取最小值，限制季檔案讀取大小，避免記憶體不足，最大1024
                is.read(bytes);
                builder.append(new String(bytes));//把讀到的東西轉為字串，並接在讀到的東西後面
                ava = is.available();//更新可以讀取的數量
            }
        } catch (IOException exp) {   //確保讀取是否崩潰，如果崩潰將顯示在run
            exp.printStackTrace();
        }
        return new Gson().fromJson(builder.toString(), ProfileData.class);//回傳profildata(gson把String轉為Class)
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

    public static File getProfile() {
        return new File(profileFolder, profilePrefix + "_profile.json");
    }

    //把所有的紀錄放在日曆中，需所有有遊玩的日期，
    public static Map<String, List<HistoryData>> getRecordFiles(UUID uuid) {
        HashMap<String, List<HistoryData>> result = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<Date> gameList = getFiles(uuid, FileType.GAME);
            gameList.forEach(date -> {
                String key = DATE_FORMAT.format(date);
                //如未有List創建新的並將資料帶入
                List<HistoryData> list = result.computeIfAbsent(key, k -> new ArrayList<>());
                list.add(new HistoryData(FileType.GAME, date));
            });
            List<Date> shortLineList = getFiles(uuid, FileType.SHORT_LINE);
            shortLineList.forEach(date -> {
                String key = DATE_FORMAT.format(date);
                List<HistoryData> list = result.computeIfAbsent(key, k -> new ArrayList<>());
                list.add(new HistoryData(FileType.SHORT_LINE, date));
            });
        }
        return result;
    }

    public static boolean deleteRecord(HistoryData data) {
        String path = String.join("/", new String[]{
                fileDir, Client.getUuid().toString(), data.type.displayName, Long.toString(data.date.getTime())
        });
        File folder = new File(path);
        System.out.println(folder.getAbsolutePath());
        if (!folder.exists()) return false;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) return false;
            }
        }
        return folder.delete();
    }

    //從資料中取得個人遊玩紀錄，根據遊戲類型取得路徑
    private static List<Date> getFiles(UUID uuid, FileType type) {
        String path = String.join("/", new String[]{
                fileDir, uuid.toString(), type.displayName
        });
        //確認資料夾是否存在
        checkFolder(path);
        //listFiles可取得資料夾中所有檔案
        File[] files = new File(path).listFiles();
        if (files == null) return new ArrayList<>();
        List<Date> fileList = new ArrayList<>(files.length);
        for (File file : files) {
            //parseLong 將字串轉換為Long
            long time = Long.parseLong(file.getName());
          //每個時間→日期(Date)、儲存每個時間成一個list(Array List)
            fileList.add(new Date(time));
        }
        //回傳
        return fileList;
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
