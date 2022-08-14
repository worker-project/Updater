package com.workerai.updater;

import com.workerai.updater.download.DownloadCallback;
import com.workerai.updater.download.DownloadManager;
import com.workerai.updater.ui.Window;
import com.workerai.updater.utils.FileManager;
import com.workerai.updater.utils.PlatformHandler;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.utils.ExternalFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static com.workerai.updater.utils.PlatformHandler.getSystemName;

public class WorkerUpdater {
    static WorkerUpdater INSTANCE;

    final Path updaterDirectory = PlatformHandler.createFolder(".WorkerAI", "bin");
    final Path downloadDirectory = PlatformHandler.createFolder(".WorkerAI", "bin", "download");
    final FileManager fileManager = new FileManager(updaterDirectory);
    final DownloadCallback downloadCallback = new DownloadCallback();
    final String DOWNLOAD_URL = "http://185.245.183.191/public/files/WorkerBootstrap/";
    static final Window window = new Window();

    public WorkerUpdater() {
        INSTANCE = this;
        if (!this.updaterDirectory.toFile().exists()) {
            new File(updaterDirectory.toString()).mkdirs();
            if (!this.updaterDirectory.toFile().mkdir()) {
                System.out.println("Unable to create updater folder!");
            }
        }

        startUpdateProcess();
    }

    public void startUpdateProcess() {
        fileManager.createFile(fileManager.javaSizeFile);
        fileManager.createFile(fileManager.zipSizeFile);

        DownloadManager downloadManager = new DownloadManager(this, getSystemName(), System.getProperty("os.arch"));
        downloadManager.genDownloadFiles();

        Runnable endDownload = () -> {
            try {
                downloadManager.unzipFiles();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        final UpdaterOptions option = new UpdaterOptions.UpdaterOptionsBuilder()
                .withExternalFileDeleter(new ExternalFileDeleter())
                .build();

        final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                .withExternalFiles(downloadManager.getExternalFiles())
                .withUpdaterOptions(option)
                .withProgressCallback(new DownloadCallback())
                .withPostExecutions(Collections.singletonList(endDownload))
                //.withLogger(LOGGER)
                .build();

        try {
            updater.update(updaterDirectory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startWorkerLauncher() {
        File launcherJar = new File(downloadDirectory.toFile(), "WorkerLauncher.jar");
        if (!launcherJar.exists()) {
            int result = JOptionPane.showConfirmDialog(null, "Launcher file doesn't exist...\nWould you like to try to download it again?", null, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                window.drawStartPage();
            } else {
                System.exit(0);
            }
        }

        System.setProperty("java.home", fileManager.javaFolder.getAbsolutePath());

        ClasspathConstructor constructor = new ClasspathConstructor();
        constructor.add(launcherJar.toPath());

        ExternalLaunchProfile profile = new ExternalLaunchProfile("com.workerai.launcher.Main", constructor.make());
        profile.setArgs(Arrays.asList("--javaPath", fileManager.javaFolder.getAbsolutePath()));
        ExternalLauncher launcher = new ExternalLauncher(profile);

        try {
            launcher.launch();
        } catch (LaunchException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    public static WorkerUpdater getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        window.drawStartPage();
    }

    public Path getUpdaterDirectory() {
        return updaterDirectory;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DownloadCallback getDownloadCallback() {
        return downloadCallback;
    }

    /*public ILogger getLogger() {
        return LOGGER;
    }*/

    public Window getWindow() {
        return window;
    }

    public String getDownloadUrl() {
        return DOWNLOAD_URL;
    }
}
