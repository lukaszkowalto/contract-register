package controlling.contractRegister.service;

import controlling.contractRegister.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${upload.path}")
    private String uploadPath;

    public void delete(File file) {
        try {
            file.delete();
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
    }

    public void save(MultipartFile file, UUID token) {
        try {
            createDirectory();
            byte[] fileBytes = file.getBytes();
            Optional<String> fileExtension = FileUtil.getFileExtension(file);
            Path path = Paths.get(uploadPath + token + "." + fileExtension.orElse("txt"));
            Files.write(path, fileBytes);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
    }

    public Resource get(String fileName) {
        try {
            Path path = getUploadPath().resolve(fileName);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("ERROR: File is not readable.");
            }
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
    }

    public Path getUploadPath() {
        return Paths.get(this.uploadPath);
    }

    private void createDirectory() {
        try {
            if (!Files.exists(Paths.get(uploadPath))) {
                Files.createDirectories(Paths.get(uploadPath));
            }
        } catch (IOException e) {
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
    }
}