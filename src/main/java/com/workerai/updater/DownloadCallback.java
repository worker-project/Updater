package com.workerai.updater;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;

import java.nio.file.Path;
import java.text.DecimalFormat;

public class DownloadCallback implements IProgressCallback {
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    @Override
    public void init(ILogger logger) {
    }

    @Override
    public void step(Step step) {
        //stepLabel.setText(StepInfo.valueOf(step.name()).getDetails());
        WorkerUpdater.getLogger().debug(StepInfo.valueOf(step.name()).getDetails());
    }

    @Override
    public void onFileDownloaded(Path path) {
        //fileLabel.setText(path.getFileName().toString());
        WorkerUpdater.getLogger().debug(path.getFileName().toString());
    }

    @Override
    public void update(DownloadList.DownloadInfo info) {
        WorkerUpdater.getLogger().debug(info.getDownloadedBytes() + " " + info.getTotalToDownloadBytes());
    }

    public enum StepInfo {
        EXTERNAL_FILES("Downloading external files..."),
        POST_EXECUTIONS("Running post executions..."),
        END("Finished!");

        final String details;

        StepInfo(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }
    }
}
