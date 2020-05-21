package pt.home.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import pt.home.exceptions.FileStorageException;
import pt.home.repositories.FileRepository;
import pt.home.utils.FileStorageProperties;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path fileStorageLocation;
	private final FileRepository fileRepository;

	public FileStorageServiceImpl(
			final FileStorageProperties fileStorageProperties,
			final FileRepository fileRepository
	) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		this.fileRepository = fileRepository;
	}

	@Override
    //Stores file in file folder
    public String storeFile(MultipartFile file) {

        final String normalizedFileName = normalizeFileName(file.getOriginalFilename());

        if (containsIllegalCharacters(normalizedFileName)) {
            throw new FileStorageException("Filename contains invalid path sequence " + normalizedFileName);
        }

        try {

            return moveFile(file, normalizedFileName);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + normalizedFileName, ex);
        }
    }

    @Override
    //returns file path by file name
    public Path getFilePathByFileName(final String fileName) {

        return this.fileStorageLocation.resolve(fileName).normalize();
    }

    //cleans filename and adds random UID
    private String normalizeFileName(final String filename) {

        return RandomStringUtils.randomAlphanumeric(10) + StringUtils.cleanPath(filename);
    }

    //checks for presence of '..'
    private boolean containsIllegalCharacters(final String text) {
        return text.contains("..");
    }

    //moves file to file folder
    private String moveFile(final MultipartFile file, final String filename) throws IOException {

        final Path targetLocation = this.fileStorageLocation.resolve(filename);

        fileRepository.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }
}
