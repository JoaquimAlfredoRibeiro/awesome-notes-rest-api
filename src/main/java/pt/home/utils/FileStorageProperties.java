package pt.home.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "file")
//class to store file storage properties
public class FileStorageProperties {
    private String uploadDir;
}