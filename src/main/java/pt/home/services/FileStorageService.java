package pt.home.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    Path getFilePathByFileName(String fileName);
}
