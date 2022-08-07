package com.workerai.updater.utils;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

public class FileManager {
    public final File javaFolder;
    public final File sizeFile;

    public FileManager(Path updaterDirectory) {
        this.javaFolder = new File(updaterDirectory.toFile(), "java");
        this.sizeFile = new File(updaterDirectory.toFile(), ".size");
    }

    public void createSizeFile() {
        try {
            if (!this.sizeFile.exists()) {
                this.sizeFile.createNewFile();
                writeSizeFile("0");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeSizeFile(String s) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(sizeFile));
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readSizeFile() {
        Scanner reader;
        try {
            reader = new Scanner(this.sizeFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return reader.nextLine();
    }

    public long getJavaFolderSize() {
        return getJavaFolderSize(this.javaFolder);
    }

    long getJavaFolderSize(File d) {
        long length = 0;
        File[] files = d.listFiles();
        int count = files == null || !d.isDirectory() ? 0 : files.length;

        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getJavaFolderSize(files[i]);
            }
        }
        return length;
    }
}
