package com.workerai.updater;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;

import java.nio.file.Path;

public class DownloadCallback implements IProgressCallback {
    @Override
    public void init(ILogger logger) {
    }

    @Override
    public void step(Step step) {
        //stepLabel.setText(StepInfo.valueOf(step.name()).getDetails());
        WorkerUpdater.getInstance().getLogger().debug(StepInfo.valueOf(step.name()).getDetails());
        int currentValue = WorkerUpdater.getInstance().getWindow().getProgressBar().getValue();
        int valueToDisplay = currentValue == 0 ? 10 : currentValue + 10;
        WorkerUpdater.getInstance().getWindow().getProgressBar().setValue(valueToDisplay);
    }

    @Override
    public void onFileDownloaded(Path path) {
        //fileLabel.setText(path.getFileName().toString());
        path.getFileName().toString();
    }

    @Override
    public void update(DownloadList.DownloadInfo info) {
        try {
            double progress = (info.getDownloadedBytes() / info.getTotalToDownloadBytes());
        } catch (ArithmeticException arEx) {
            return;
        }
    }

    public enum StepInfo {
        START_DOWNLOAD("Downloading files..."),
        END_DOWNLOAD("Successfully downloaded files!\n"),

        START_CHECK("Preparing and checking files...\n"),
        END_CHECK("Successfully prepared and checked files!\n"),

        START_UNZIP("Unzipping java version..."),
        END_UNZIP("Successfully unzipped java version!\n"),

        START_RENAME("Renaming unzipped java version..."),
        END_RENAME("Successfully renamed unzipped java version!\n");

        final String details;

        StepInfo(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }
    }
}
