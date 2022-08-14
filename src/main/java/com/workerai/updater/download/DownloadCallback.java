package com.workerai.updater.download;

import com.workerai.updater.WorkerUpdater;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;

import static com.workerai.updater.utils.ThrowWait.throwWait;

public class DownloadCallback implements IProgressCallback {
    private boolean isUpdate = false;

    @Override
    public void init(ILogger logger) {
    }

    @Override
    public void step(Step step) {
        throwWait(2000);
        WorkerUpdater.getInstance().getWindow().getProgressLabel().setText(StepInfo.valueOf(step.name()).getDetails());
        WorkerUpdater.getInstance().getWindow().progressTextFormatter();

        if (step.equals(Step.START_DOWNLOAD)) {
            isUpdate = true;
            WorkerUpdater.getInstance().getWindow().drawUpdatePage();
        } else if (step.equals(Step.START_CHECK) && !isUpdate) {
            WorkerUpdater.getInstance().getWindow().drawUpdatePage();
        } else if (step.equals(Step.END_CHECK)) {
            isUpdate = false;
            displayProgressValue(StepInfo.valueOf(step.name()).toString());
            WorkerUpdater.getInstance().startWorkerLauncher();
        }

        displayProgressValue(StepInfo.valueOf(step.name()).toString());
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

    void displayProgressValue(String step) {
        int valueToDisplay = 0;

        switch (step) {
            case "START_DOWNLOAD":
                valueToDisplay = 10;
                break;
            case "END_DOWNLOAD":
                valueToDisplay = 20;
                break;

            case "START_CHECK":
                valueToDisplay = 30;
                break;
            case "END_CHECK":
                valueToDisplay = 80;
                break;

            case "START_RENAME":
                valueToDisplay = 60;
                break;
            case "END_RENAME":
                valueToDisplay = 70;
                break;

            case "START_UNZIP":
                valueToDisplay = 40;
                break;
            case "END_UNZIP":
                valueToDisplay = 50;
                break;
        }

        WorkerUpdater.getInstance().getWindow().getProgressBar().setValue(valueToDisplay);
    }
}
