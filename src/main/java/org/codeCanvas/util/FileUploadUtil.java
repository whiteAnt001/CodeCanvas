package org.codeCanvas.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads/";

    // 단일파일 저장
    public static String saveFile(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty())
            return null;

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.write(filePath, file.getBytes());

        return fileName;
    }

    // 여러 파일 저장 (혹시 모르니까 만들어 놨음)
    public static String[] saveFiles(MultipartFile[] files) throws IOException {
        if(files == null || files.length == 0)
            return new String[]{};

        String[] fileNames = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            fileNames[i] = saveFile(files[i]);
        }

        return fileNames;
    }
}
