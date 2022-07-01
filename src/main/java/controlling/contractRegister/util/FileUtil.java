package controlling.contractRegister.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class FileUtil {

    public static Optional<String> getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
    }
}