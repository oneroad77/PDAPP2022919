package com.example.pdapp2022919.SystemManager;

public enum FileType {

    PRETEST("game", "pretest"),
    POSTTEST("game", "posttest"),
    GAME_ONE("game", "game1"),
    GAME_TWO("game", "game2"),
    GAME_THREE("game", "game3"),
    SHORT_LINE("short_line", "short_line");

    public final String fileType;
    public final String fileName;

    FileType(String type, String name) {
        this.fileType = type;
        this.fileName = name;
    }

}
