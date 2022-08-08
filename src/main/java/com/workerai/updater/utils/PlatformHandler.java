package com.workerai.updater.utils;

import fr.flowarg.flowcompat.Platform;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformHandler {
    public static Path createFolder(String... folderName) {
        Path path;
        switch (Platform.getCurrentPlatform()) {
            case WINDOWS:
                path = Paths.get(System.getenv("APPDATA"));
                break;
            case MAC:
                path = Paths.get(System.getProperty("user.home"), "/Library/Application Support/*");
                break;
            case LINUX:
                path = Paths.get(System.getProperty("user.home"), ".local/share");
            default:
                path = Paths.get(System.getProperty("user.home"));
        }

        path = Paths.get(path.toString(), folderName);
        return path;
    }

    public static String getSystemName() {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("win")) {
            OS = "windows";
        } else if (OS.contains("mac")) {
            OS = "macos";
        } else if (OS.contains("nix") || OS.contains("nux")) {
            OS = "linux";
        } else if (OS.contains("aix")) {
            OS = "aix";
        } else if (OS.contains("qnx")) {
            OS = "qnx";
        } else if (OS.contains("sunos")) {
            OS = "solaris";
        }

        return OS;
    }
}
