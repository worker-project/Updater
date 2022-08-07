package com.workerai.updater.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workerai.updater.WorkerUpdater;
import fr.flowarg.flowio.FileUtils;
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
    private final String os;
    private final String arch;
    private final ArrayList<ExternalFile> externalFiles = new ArrayList<>();
    private final WorkerUpdater workerUpdater;

    public DownloadManager(WorkerUpdater updater, String os, String arch) {
        this.os = os;
        this.arch = arch;

        this.workerUpdater = updater;
    }

    public void addFiles(ExternalFile... fileData) {
        this.externalFiles.addAll(Arrays.asList(fileData));
    }

    public void removeFiles(ExternalFile... fileData) {
        this.externalFiles.removeAll(Arrays.asList(fileData));
    }

    public void genDownloadFiles() {
        addFiles(getJavaFile());
    }


    ExternalFile getJavaFile() {
        String response = sendRequest();

        JsonObject json = JsonParser.parseString(response).getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject();

        String url = json.get("links").getAsJsonObject().get("pkg_download_redirect").getAsString();
        String name = json.get("filename").getAsString();
        long size = json.get("size").getAsLong();

        String urlSha256 = json.get("links").getAsJsonObject().get("pkg_info_uri").getAsString();
        String infos = getInfosFile(urlSha256);
        JsonObject jsoninfo = JsonParser.parseString(infos).getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject();
        String sha256 = jsoninfo.get("checksum").getAsString();

        return new ExternalFile(new File(workerUpdater.getUpdaterDirectory().toFile(), name).getPath(), url, sha256, size);
    }

    String sendRequest() {
        try {
            URL url = new URL(String.format("https://api.foojay.io/disco/v3.0/packages/jdks?version=17.0.1&distro=zulu&architecture=%s&archive_type=zip&operating_system=%s&javafx_bundled=true", this.arch, this.os));
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
            System.out.println("Java version not found for current OS: " + os + " and current architecture: " + arch);
            System.exit(0);
            return null;
        }
    }

    String getInfosFile(String link) {
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
            WorkerUpdater.getLogger().debug("Java version not found for current OS: " + os + " and current architecture: " + arch);
            System.exit(0);
            return null;
        }
    }

    public void unzipFiles() throws IOException {
        Path p = null;
        for (ExternalFile file : externalFiles) {
            if (file.getPath().contains(".zip")) {
                p = Paths.get(file.getPath());
            }
        }

        FileManager fileManager = workerUpdater.getFileManager();
        if (p != null) {
            if (!fileManager.javaFolder.exists() || (fileManager.javaFolder.exists() && !fileManager.readSizeFile().equals(Long.toString(fileManager.getJavaFolderSize())))) {
                FileUtils.deleteDirectory(fileManager.javaFolder.toPath());
                ZipUtils.unzip(workerUpdater.getUpdaterDirectory(), p);
                File newFolder = Paths.get(p.toString().substring(0, p.toString().length() - 4)).toFile();

                if (newFolder.exists() && !fileManager.javaFolder.exists()) {
                    Files.move(newFolder.toPath(), fileManager.javaFolder.toPath());
                }

                fileManager.writeSizeFile(Long.toString(fileManager.getJavaFolderSize()));
            }
        }
    }

    public ArrayList<ExternalFile> getExternalFiles() {
        return externalFiles;
    }
}
