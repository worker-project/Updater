package com.workerai.updater;

import com.workerai.updater.utils.DownloadManager;
import com.workerai.updater.utils.FileManager;
import com.workerai.updater.utils.PlatformManager;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.utils.ExternalFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.workerai.updater.utils.PlatformManager.getSystemName;

public class WorkerUpdater {
    private final Path updaterDirectory = PlatformManager.createFolder(".WorkerAI", "bin");
    private final FileManager fileManager = new FileManager(updaterDirectory);
    private static ILogger LOGGER;

    public WorkerUpdater() throws IOException {
        if (!updaterDirectory.getParent().toFile().exists()) {
            if (!updaterDirectory.getParent().toFile().mkdir()) {
                System.out.println("Unable to create root app folder");
            }
        }
        if (!updaterDirectory.toFile().exists()) {
            if (!updaterDirectory.toFile().mkdir()) {
                System.out.println("Unable to create bin app folder");
            }
        }

        final Path logsFolder = new File(updaterDirectory.toFile(), "logs.log").toPath();
        if (!logsFolder.toFile().exists()) {
            if (!logsFolder.toFile().createNewFile()) {
                System.out.println("Unable to create logs file");
                System.exit(-1);
            }
        }
        LOGGER = new Logger("[WUpdater]", logsFolder);

        fileManager.createSizeFile();

        DownloadManager downloadManager = new DownloadManager(this, getSystemName(), System.getProperty("os.arch"));
        downloadManager.genDownloadFiles();

        final UpdaterOptions option = new UpdaterOptions.UpdaterOptionsBuilder()
                .withExternalFileDeleter(new ExternalFileDeleter())
                .build();

        final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                .withExternalFiles(downloadManager.getExternalFiles())
                .withUpdaterOptions(option)
                .withProgressCallback(new DownloadCallback())
                .withLogger(LOGGER)
                .build();

        try {
            updater.update(updaterDirectory);
            downloadManager.unzipFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        new WorkerUpdater();
    }

    public Path getUpdaterDirectory() {
        return updaterDirectory;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public static ILogger getLogger() {
        return LOGGER;
    }
}
