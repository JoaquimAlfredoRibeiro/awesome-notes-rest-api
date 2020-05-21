package pt.home.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component
public class FileRepository {

	public long copy(
			final InputStream in,
			final Path target,
			final CopyOption... options
	) throws IOException {

		return Files.copy(in, target, options);
	}
}

