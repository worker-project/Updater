package com.workerai.updater.download;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workerai.updater.WorkerUpdater;
import com.workerai.updater.utils.FileManager;
import fr.flowarg.flowio.FileUtils;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowzipper.ZipUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class DownloadManager {
    final String os;
    final String arch;
    final ArrayList<ExternalFile> externalFiles = new ArrayList<>();
    final WorkerUpdater workerUpdater;

    public DownloadManager(WorkerUpdater updater, String os, String arch) {
        this.os = os;
        this.arch = arch;

        this.workerUpdater = updater;
    }

    public void addFiles(ExternalFile fileData) {
        this.externalFiles.add(fileData);
    }

    public void removeFiles(ExternalFile... fileData) {
        this.externalFiles.removeAll(Arrays.asList(fileData));
    }

    public void genDownloadFiles() {
        addFiles(getJavaFile());
        for (ExternalFile extFile : ExternalFile.getExternalFilesFromJson(WorkerUpdater.getInstance().getDownloadUrl())) {
            addFiles(extFile);
        }
    }


    ExternalFile getJavaFile() {
        String javaResponse = getJavaVersionFromInfo();

        JsonObject json = JsonParser.parseString(javaResponse).getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject();

        String url = json.get("links").getAsJsonObject().get("pkg_download_redirect").getAsString();
        String name = json.get("filename").getAsString();
        long size = json.get("size").getAsLong();

        String urlSha256 = json.get("links").getAsJsonObject().get("pkg_info_uri").getAsString();
        String infos = getJavaFileInfo(urlSha256);
        JsonObject jsoninfo = JsonParser.parseString(infos).getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject();
        String sha256 = jsoninfo.get("checksum").getAsString();

        return new ExternalFile(new File(workerUpdater.getUpdaterDirectory().toFile(), name).getPath(), url, sha256, size, true);
    }

    String getJavaVersionFromInfo() {
        try {
            URL url = new URL(String.format("https://api.foojay.io/disco/v3.0/packages/jdks?version=17.0.1&distro=zulu&architecture=%s&archive_type=zip&operating_system=%s&javafx_bundled=false", this.arch, this.os));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.setDoOutput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (IOException e) {
            System.out.println("\u001B[31mJava version not found for current OS: \u001B[0m" + os + " \u001B[31mand current architecture: \u001B[0m" + arch);
            System.exit(0);
            return null;
        }
    }

    String getJavaFileInfo(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();

        } catch (IOException e) {
            System.out.println("\u001B[31mJava version not found for current OS: \u001B[0m" + os + " \u001B[31mand current architecture: \u001B[0m" + arch);
            System.exit(0);
            return null;
        }
    }

    void checkFiles() {
        ArrayList<Path> paths = new ArrayList<>();
        paths.add(WorkerUpdater.getInstance().getFileManager().javaSizeFile.toPath());
        paths.add(WorkerUpdater.getInstance().getFileManager().zipSizeFile.toPath());
        paths.add(WorkerUpdater.getInstance().getUpdaterDirectory().resolve("logs.log"));

        for (ExternalFile extFile : externalFiles) {
            paths.add(Paths.get(extFile.getPath()));
        }

        for(File files : workerUpdater.getUpdaterDirectory().toFile().listFiles()) {
            if(!paths.contains(Paths.get(files.getPath()))) {
                files.delete();
            }
        }

    }

    public void unzipFiles() throws IOException {
        checkFiles();

        Path p = null;
        for (ExternalFile file : externalFiles) {
            if (file.getPath().contains(".zip")) {
                p = Paths.get(file.getPath());
            }
        }
        FileManager fileManager = workerUpdater.getFileManager();
        if (p != null) {
            if (!fileManager.javaFolder.exists() ||
                    (fileManager.javaFolder.exists() && !fileManager.readSizeFile(fileManager.javaSizeFile).equals(Long.toString(fileManager.getJavaFolderSize())))
            || (p.toFile().exists() && !fileManager.readSizeFile(fileManager.zipSizeFile).equals(Long.toString(fileManager.getFolderSize(p.toFile()))))) {

                FileUtils.deleteDirectory(fileManager.javaFolder.toPath());

                WorkerUpdater.getInstance().getDownloadCallback().step(Step.START_UNZIP);
                ZipUtils.unzip(workerUpdater.getUpdaterDirectory(), p);
                WorkerUpdater.getInstance().getDownloadCallback().step(Step.END_UNZIP);

                WorkerUpdater.getInstance().getDownloadCallback().step(Step.START_RENAME);
                File newFolder = Paths.get(p.toString().substring(0, p.toString().length() - 4)).toFile();
                if (newFolder.exists() && !fileManager.javaFolder.exists()) {
                    Files.move(newFolder.toPath(), fileManager.javaFolder.toPath());
                }
                WorkerUpdater.getInstance().getDownloadCallback().step(Step.END_RENAME);

                fileManager.writeSizeFile(fileManager.javaSizeFile, Long.toString(fileManager.getJavaFolderSize()));
                fileManager.writeSizeFile(fileManager.zipSizeFile, Long.toString(fileManager.getFolderSize(p.toFile())));
            }
        }
    }

    public ArrayList<ExternalFile> getExternalFiles() {
        return externalFiles;
    }
}
