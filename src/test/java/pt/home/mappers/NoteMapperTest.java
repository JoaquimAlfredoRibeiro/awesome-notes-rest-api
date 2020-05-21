package pt.home.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

import pt.home.api.v1.mapper.NoteMapper;
import pt.home.api.v1.model.NoteDTO;
import pt.home.domain.Note;
import pt.home.instanceprovers.NoteDTOInstanceProvider;
import pt.home.instanceprovers.NoteInstanceProvider;

public class NoteMapperTest {

	private static final String CONTENT = "Example note content.";

	@Test
	void noteToNoteDTO_whenNoteIsNotNull_returnsConvertedNote() {

		final Note expectedNote = new NoteInstanceProvider()
				.withContent(CONTENT)
				.getInstance();

		final NoteDTO convertedNoteDto = NoteMapper.noteToNoteDTO(expectedNote);

		assertAll(
				() -> assertThat(convertedNoteDto).isEqualToIgnoringGivenFields(expectedNote,
						"image64",
						"active",
						"createdBy",
						"updatedBy",
						"createdAt",
						"updatedAt"),

				() -> assertThat(convertedNoteDto.getImage64()).isNull()
		);
	}

	@Test
	void noteToNoteDTO_whenNoteIsNull_returnsNull() {

		assertThat(NoteMapper.noteToNoteDTO(null)).isNull();
	}

	@Test
	public void noteDTOToNote_whenNoteIsNotNullReturnsConvertedNotedDto() {


		final NoteDTO expectedNoteDTO = new NoteDTOInstanceProvider()
				.withContent(CONTENT)
				.getInstance();

		final Note convertedNote = NoteMapper.noteDTOToNote(expectedNoteDTO);

		assertThat(convertedNote).isEqualToIgnoringGivenFields(expectedNoteDTO,
				"image64",
				"active",
				"createdBy",
				"updatedBy",
				"createdAt",
				"updatedAt");
	}

	@Test
	void noteDTOToNote_whenNoteDtoIsNull_returnsNull() {

		assertThat(NoteMapper.noteDTOToNote(null)).isNull();
	}
}
