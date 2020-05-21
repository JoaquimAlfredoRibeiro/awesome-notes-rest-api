package pt.home.api.v1.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pt.home.api.v1.model.NoteDTO;
import pt.home.domain.Note;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoteMapper {

	//Map from Note to NoteDTO
	public static NoteDTO noteToNoteDTO(Note note) {
		if (note == null) {
			return null;
		}

		final NoteDTO noteDTO = new NoteDTO();
		noteDTO.setId(note.getId());
		noteDTO.setContent(note.getContent());
		noteDTO.setFinished(note.getFinished());
		noteDTO.setImageName(note.getImageName());
		noteDTO.setDueDate(note.getDueDate());

		return noteDTO;
	}

	//Map from NoteDTO to Note, always set active to 'true' in this instance
	public static Note noteDTOToNote(NoteDTO noteDTO) {
		if (noteDTO == null) {
			return null;
		}

		final Note note = new Note();
		note.setId(noteDTO.getId());
		note.setContent(noteDTO.getContent());
		note.setFinished(noteDTO.getFinished());
		note.setImageName(noteDTO.getImageName());
		note.setDueDate(noteDTO.getDueDate());
		note.setActive(Boolean.TRUE);

		return note;
	}
}
