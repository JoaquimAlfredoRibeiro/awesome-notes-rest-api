package pt.home.services;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import pt.home.exceptions.FileStorageException;
import pt.home.repositories.FileRepository;
import pt.home.utils.FileStorageProperties;

class FileStorageServiceImplTest {

	private static final String UPLOAD_FOLDER = "uploadFolder";
	private static final String VALID_FILE_NAME = "filename.jpg";
	private static final String INVALID_FILE_NAME = "filename..jpg";

	private FileStorageServiceImpl fileStorageService;

	@Mock
	private FileRepository fileRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		fileStorageService = new FileStorageServiceImpl(getFileStorageProperties(), fileRepository);
	}

	@Test
	void storeFile_whenFileIsCorrect_storesFile_andReturnNormalizedFileName() throws IOException {

		final MockMultipartFile file = getDummyImageMultiPartFile(VALID_FILE_NAME);

		when(fileRepository.copy(
				eq(file.getInputStream()),
				any(Path.class),
				eq(StandardCopyOption.REPLACE_EXISTING))).thenReturn(1L);

		final String result = fileStorageService.storeFile(file);

		assertThat(result).isNotBlank();
		assertThat(result).contains(VALID_FILE_NAME);
	}

	@Test
	void storeFile_whenFileNameIsIncorrect_throwsFileStorageException() throws IOException {

		final MockMultipartFile file = getDummyImageMultiPartFile(INVALID_FILE_NAME);

		when(fileRepository.copy(
				eq(file.getInputStream()),
				any(Path.class),
				eq(StandardCopyOption.REPLACE_EXISTING))).thenReturn(1L);

		final Exception exceptionThrowed = assertThrows(FileStorageException.class,
				() -> fileStorageService.storeFile(file));

		assertThat(exceptionThrowed.getMessage()).isNotBlank();
	}

	@Test
	void storeFile_whenSavingThrowsException_throwsFileStorageException() throws IOException {

		final MockMultipartFile file = getDummyImageMultiPartFile(INVALID_FILE_NAME);

		when(fileRepository.copy(
				eq(file.getInputStream()),
				any(Path.class),
				eq(StandardCopyOption.REPLACE_EXISTING))).thenThrow(IOException.class);

		final Exception exceptionThrowed = assertThrows(FileStorageException.class,
				() -> fileStorageService.storeFile(file));

		assertThat(exceptionThrowed.getMessage()).isNotBlank();
	}


	private FileStorageProperties getFileStorageProperties() {

		final FileStorageProperties fileStorageProperties = new FileStorageProperties();
		fileStorageProperties.setUploadDir(UPLOAD_FOLDER);
		return fileStorageProperties;
	}

	private MockMultipartFile getDummyImageMultiPartFile(final String filename) {
		return new MockMultipartFile(
				"file",
				filename,
				MediaType.IMAGE_JPEG_VALUE,
				"<<jpegData>>".getBytes(StandardCharsets.UTF_8));
	}
}
