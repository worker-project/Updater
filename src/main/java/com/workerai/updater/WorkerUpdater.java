package com.workerai.updater;

import com.workerai.updater.ui.Window;
import com.workerai.updater.utils.DownloadManager;
import com.workerai.updater.utils.FileManager;
import com.workerai.updater.utils.PlatformHandler;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.utils.ExternalFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static com.workerai.updater.utils.PlatformHandler.getSystemName;

public class WorkerUpdater {
    private static WorkerUpdater INSTANCE;
    private final ILogger LOGGER;
    private final Path updaterDirectory = PlatformHandler.createFolder(".WorkerAI", "bin");
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

        new Thread(this::startUpdateProcess).start();
    }

    public void startUpdateProcess() {
        window.drawUpdatePage();

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

        window.drawEndPage();
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
