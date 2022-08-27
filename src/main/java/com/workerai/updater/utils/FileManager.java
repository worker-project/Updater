package com.workerai.updater.utils;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

public class FileManager {
    public final File javaFolder;
    public final File javaSizeFile;
    public final File zipSizeFile;

    public FileManager(Path updaterDirectory) {
        this.javaFolder = new File(updaterDirectory.toFile(), "java");
        this.javaSizeFile = new File(updaterDirectory.toFile(), "javaSize");
        this.zipSizeFile = new File(updaterDirectory.toFile(), "zipSize");

    }

    public void createFileSize(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
                saveSizeInFile(file, "0");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSizeInFile(File file, String size) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(size);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFileSize(File file) {
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return reader.nextLine();
    }

    public long getJavaFolderSize() {
        return getFolderSize(this.javaFolder);
    }

    public long getFolderSize(File directory) {
        long length = 0;
        File[] files = directory.listFiles();

        if (!directory.isDirectory()) {
            return directory.length();
        }

        int count = files == null ? 0 : files.length;

        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }
}
