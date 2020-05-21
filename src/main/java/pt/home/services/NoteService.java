package pt.home.services;

import pt.home.api.v1.model.NoteDTO;
import pt.home.domain.Note;

import java.util.List;

public interface NoteService {

    List<NoteDTO> getAllNotes(Long id);

    List<NoteDTO> getAllNotes(Long id, boolean finished);

    List<NoteDTO> getNotesForToday(final Long id);

    NoteDTO getNoteDTOById(Long noteId, Long userId);

    NoteDTO createNote(NoteDTO noteDTO);

    NoteDTO saveNoteByDTO(NoteDTO noteDTO);

    NoteDTO deleteNoteById(Long id);

    NoteDTO convertNoteToNoteDtoWithImage(Note note);
}
