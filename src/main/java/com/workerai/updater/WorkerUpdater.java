package com.workerai.updater;

import com.workerai.updater.ui.Window;
import com.workerai.updater.utils.DownloadManager;
import com.workerai.updater.utils.FileManager;
import com.workerai.updater.utils.PlatformHandler;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.utils.ExternalFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static com.workerai.updater.utils.PlatformHandler.getSystemName;

public class WorkerUpdater extends JPanel {
    private static WorkerUpdater INSTANCE;
    private final ILogger LOGGER;
    private final Path updaterDirectory = PlatformHandler.createFolder(".WorkerAI", "bin");
    private final Path downloadDirectory = PlatformHandler.createFolder(".WorkerAI", "bin", "download");
    private final FileManager fileManager = new FileManager(updaterDirectory);
    private final DownloadCallback downloadCallback = new DownloadCallback();

    private static final Window window = new Window();

    public WorkerUpdater() {
        INSTANCE = this;
        this.LOGGER = new Logger("[WorkerAI]", updaterDirectory.resolve("logs.log"));
        if (!this.updaterDirectory.toFile().exists()) {
            if (!this.updaterDirectory.toFile().mkdir()) {
                this.LOGGER.err("Unable to create updater folder");
            }
        }

        startUpdateProcess();
    }

    public void startUpdateProcess() {
        fileManager.createSizeFile();

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
                .withLogger(LOGGER)
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

    public static WorkerUpdater getInstance() {
        return INSTANCE;
    }

    public ILogger getLogger() {
        return LOGGER;
    }

    public Window getWindow() {
        return window;
    }
}
