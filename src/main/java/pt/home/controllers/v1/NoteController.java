package pt.home.controllers.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pt.home.api.v1.model.NoteDTO;
import pt.home.security.user.UserPrincipal;
import pt.home.services.NoteService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(NoteController.BASE_URL)
@RequiredArgsConstructor
public class NoteController {

    public static final String BASE_URL = "/api/v1/note";

    private final NoteService noteService;

    //returns all user notes
    @GetMapping({"/all"})
    public ResponseEntity<List<NoteDTO>> getAllNotes(@AuthenticationPrincipal final UserPrincipal currentUser) {

        return new ResponseEntity<>(noteService.getAllNotes(currentUser.getId()), HttpStatus.OK);
    }

    @GetMapping({"/all/{finished}"})
    //returns all user notes by finished property
    public ResponseEntity<List<NoteDTO>> getAllNotesByFinished(@AuthenticationPrincipal final UserPrincipal currentUser, @PathVariable final boolean finished) {

        return new ResponseEntity<>(noteService.getAllNotes(currentUser.getId(), finished), HttpStatus.OK);
    }

    @GetMapping({"/all/today"})
    //returns all user notes with due date of 'TODAY'
    public ResponseEntity<List<NoteDTO>> getAllNotesForToday(@AuthenticationPrincipal final UserPrincipal currentUser) {

        return new ResponseEntity<>(noteService.getNotesForToday(currentUser.getId()), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})
    //returns Note by Id
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable final Long id, @AuthenticationPrincipal final UserPrincipal currentUser) {

        return new ResponseEntity<>(noteService.getNoteDTOById(id, currentUser.getId()), HttpStatus.OK);
    }

    @PostMapping
    //creates new Note and returns NoteDTO
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody final NoteDTO noteDTO) {

        return new ResponseEntity<>(noteService.createNote(noteDTO), HttpStatus.CREATED);
    }

    @PutMapping
    //updates Note and returns NoteDTO
    public ResponseEntity<NoteDTO> updateNote(@Valid @RequestBody final NoteDTO noteDTO) {

        return new ResponseEntity<>(noteService.saveNoteByDTO(noteDTO), HttpStatus.OK);
    }

    @DeleteMapping({"/{id}"})
    //sets Note 'active' to false and returns NoteDTO
    public ResponseEntity<NoteDTO> deleteNote(@PathVariable final Long id) {

        return new ResponseEntity<>(noteService.deleteNoteById(id), HttpStatus.OK);
    }
}
