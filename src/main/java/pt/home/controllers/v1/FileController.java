package pt.home.controllers.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.home.api.v1.model.NoteDTO;
import pt.home.domain.Note;
import pt.home.exceptions.FileStorageException;
import pt.home.exceptions.ResourceNotFoundException;
import pt.home.repositories.NoteRepository;
import pt.home.services.FileStorageService;
import pt.home.services.NoteService;

@RestController
@RequestMapping(FileController.BASE_URL)
@RequiredArgsConstructor
public class FileController {

    public static final String BASE_URL = "/api/v1/files";

    private final NoteRepository noteRepository;
    private final NoteService noteService;
    private final FileStorageService fileStorageService;

    @PostMapping({"/upload/{id}"})
    @ResponseStatus(HttpStatus.OK)
    //uploads image to file folder and updates note with image name
    public ResponseEntity<NoteDTO> uploadImage(@PathVariable final Long id, @RequestParam("file") MultipartFile file) {

        //check if file is an image
        if (!file.getContentType().startsWith("image")) {
            throw new FileStorageException("File is not an image");
        }

        //store the file in file folder and returns generated file name
        String fileName = fileStorageService.storeFile(file);

        //update note with generated image name
        Note updatedNote = noteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedNote.setImageName(fileName);
        updatedNote = noteRepository.save(updatedNote);

        //converts updated Note to NoteDTO to return to client
        NoteDTO noteDTO = noteService.convertNoteToNoteDtoWithImage(updatedNote);

        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    //updates note with no image name
    @PostMapping({"/delete/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NoteDTO> uploadImage(@PathVariable final Long id) {
        //update note with no image name
        Note updatedNote = noteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedNote.setImageName(null);
        updatedNote = noteRepository.save(updatedNote);

        NoteDTO noteDTO = noteService.convertNoteToNoteDtoWithImage(updatedNote);

        //converts updated Note to NoteDTO to return to client
        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }
}
