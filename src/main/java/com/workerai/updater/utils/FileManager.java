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
        this.javaSizeFile = new File(updaterDirectory.toFile(), "size");
        this.zipSizeFile = new File(updaterDirectory.toFile(), "zsize");

    }

    public void createFile(File f) {
        try {
            if (!f.exists()) {
                f.createNewFile();
                writeSizeFile(f, "0");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeSizeFile(File f, String s) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readSizeFile(File file) {
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

    public long getFolderSize(File d) {
        long length = 0;
        File[] files = d.listFiles();

        if (!d.isDirectory()) {
            return d.length();
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
