package pt.home.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pt.home.api.v1.mapper.NoteMapper;
import pt.home.api.v1.model.NoteDTO;
import pt.home.domain.Note;
import pt.home.exceptions.ResourceNotFoundException;
import pt.home.repositories.NoteRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final FileStorageService fileStorageService;

    private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Override
    //returns all active notes of given User as NoteDTO
    public List<NoteDTO> getAllNotes(final Long id) {

        return noteRepository.findAll()
                .stream()
                .filter(note -> note.getCreatedBy().equals(id))
                .filter(Note::getActive)
                .map(this::convertNoteToNoteDtoWithImage)
                .sorted(Comparator.comparing(NoteDTO::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    //returns all active notes of given User as NoteDTO, by finished parameter
    public List<NoteDTO> getAllNotes(final Long id, final boolean finished) {

        return noteRepository.findAll()
                .stream()
                .filter(note -> note.getCreatedBy().equals(id))
                .filter(Note::getActive)
                .filter(note -> note.getFinished().equals(finished))
                .map(this::convertNoteToNoteDtoWithImage)
                .sorted(Comparator.comparing(NoteDTO::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    //returns all active, unfinished notes of given User as NoteDTO with dueDate of 'TODAY'
    public List<NoteDTO> getNotesForToday(final Long id) {

        return noteRepository.findAll()
                .stream()
                .filter(note -> note.getCreatedBy().equals(id))
                .filter(note -> note.getDueDate() != null && note.getDueDate().toLocalDate().isEqual(LocalDate.now()))
                .filter(Note::getActive)
                .filter(note -> note.getFinished().equals(false))
                .map(this::convertNoteToNoteDtoWithImage)
                .sorted(Comparator.comparing(NoteDTO::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    //returns active note of given User by Id
    public NoteDTO getNoteDTOById(final Long noteId, final Long userId) {

        return noteRepository.findById(noteId)
                .filter(note -> note.getCreatedBy().equals(userId))
                .filter(Note::getActive)
                .map(NoteMapper::noteToNoteDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    //creates new note
    public NoteDTO createNote(final NoteDTO noteDTO) {

        return saveAndReturnDTO(NoteMapper.noteDTOToNote(noteDTO));
    }

    private NoteDTO saveAndReturnDTO(final Note note) {

        final Note savedNote = noteRepository.save(note);

        return NoteMapper.noteToNoteDTO(savedNote);
    }

    @Override
    //updates note
    public NoteDTO saveNoteByDTO(final NoteDTO noteDTO) {

        Note savedNote = noteRepository.save(NoteMapper.noteDTOToNote(noteDTO));

        return convertNoteToNoteDtoWithImage(savedNote);
    }

    @Override
    //sets note 'active' parameter to false
    public NoteDTO deleteNoteById(final Long id) {

        Note noteToDelete = noteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        noteToDelete.setActive(Boolean.FALSE);
        Note deletedNote = noteRepository.save(noteToDelete);

        return NoteMapper.noteToNoteDTO(deletedNote);
    }

    @Override
    //converts note to noteDTO with encoded image64 if the note has an image
    public NoteDTO convertNoteToNoteDtoWithImage(Note note) {
        if (note == null) {
            return null;
        } else {
            String image64 = "";
            if (note.getImageName() != null) {
                try {
                    Path filePath = fileStorageService.getFilePathByFileName(note.getImageName());
                    byte[] fileContent = FileUtils.readFileToByteArray(new File(String.valueOf(filePath)));
                    image64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }

            return NoteDTOStaticFactory.getInstance(
                    note.getId(),
                    note.getContent(),
                    note.getFinished(),
                    note.getImageName(),
                    note.getDueDate(),
                    image64
            );
        }
    }
}
