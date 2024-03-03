package br.com.engineerchallenge.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

    public static File moveAndReplaceFile(File file, String pathFrom) throws IOException {
        Path sourceFile = file.toPath();
        Path targetDirectory = Path.of(pathFrom);
        Path targetFile = targetDirectory.resolve(file.getName());
        Files.move(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);

        return targetFile.toFile();
    }

    public static String getFileContent(File file) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            log.error("Error ao tentar ler conteudo do arquivo {}", file.getAbsolutePath());
            log.error("Error {}", e.getMessage());
        }
        return content;
    }
}

