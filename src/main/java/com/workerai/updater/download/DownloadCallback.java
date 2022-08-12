package com.workerai.updater.download;

import com.workerai.updater.WorkerUpdater;
import com.workerai.updater.ui.component.bar.ProgressBar;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;

import java.nio.file.Path;

import static com.workerai.updater.utils.ThrowWait.throwWait;

public class DownloadCallback implements IProgressCallback {

    private boolean isUpdate = false;

    @Override
    public void init(ILogger logger) {
    }

    @Override
    public void step(Step step) {
        throwWait(2000);
        WorkerUpdater.getInstance().getLogger().debug(StepInfo.valueOf(step.name()).getDetails());
        WorkerUpdater.getInstance().getWindow().getProgressLabel().setText(StepInfo.valueOf(step.name()).getDetails());

        if(step.equals(Step.START_CHECK) && !isUpdate) {
            WorkerUpdater.getInstance().getWindow().drawUpdatePage();
        }

        if(step.equals(Step.START_DOWNLOAD)) {
            isUpdate = true;
            WorkerUpdater.getInstance().getWindow().drawUpdatePage();
        } else if (step.equals(Step.END_CHECK)) {
            isUpdate = false;
            displayProgressValue();
            throwWait(2000);
            WorkerUpdater.getInstance().startWorkerLauncher();
        }

        displayProgressValue();
    }

    @Override
    public void onFileDownloaded(Path path) {
        //fileLabel.setText(path.getFileName().toString());
        path.getFileName().toString();
    }

    @Override
    public void update(DownloadList.DownloadInfo info) {
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

    void displayProgressValue() {
        ProgressBar progressBar = WorkerUpdater.getInstance().getWindow().getProgressBar();
        int currentValue = progressBar.getValue();
        int valueToDisplay = isUpdate ? currentValue == 0 ? 10 : currentValue + 10 : currentValue == 0 ? progressBar.getMaximum()/2 : currentValue + progressBar.getMaximum()/2;
        WorkerUpdater.getInstance().getWindow().getProgressBar().setValue(valueToDisplay);
    }
}
