package pt.home.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pt.home.api.v1.model.NoteDTO;
import pt.home.domain.Note;
import pt.home.domain.auth.User;
import pt.home.exceptions.ResourceNotFoundException;
import pt.home.instanceprovers.NoteDTOInstanceProvider;
import pt.home.instanceprovers.NoteInstanceProvider;
import pt.home.instanceprovers.UserInstanceProvider;
import pt.home.repositories.NoteRepository;

public class NoteServiceImplTest {

	NoteService noteService;
	FileStorageService fileStorageService;

	@Mock
	NoteRepository noteRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		noteService = new NoteServiceImpl(noteRepository, fileStorageService);
	}

	@Test
	public void getAllNotes_whenUserHasNotes_returnsAllUserNotes() {
		final User suppliedUser = new UserInstanceProvider().getInstanceWithId();
		final Note note1 = new NoteInstanceProvider().withImageName(null).getInstanceWithCreatedBy();
		final Note note2 = new NoteInstanceProvider().withImageName(null).getInstanceWithCreatedBy();
		final Note note3 = new NoteInstanceProvider().withImageName(null).withCreatedBy(2L).getInstanceWithCreatedBy();
		final List<Note> notes = List.of(note1, note2, note3);

		when(noteRepository.findAll()).thenReturn(notes);

		final List<NoteDTO> expectedNoteDTOS = noteService.getAllNotes(suppliedUser.getId());

		assertAll(
				() -> assertThat(expectedNoteDTOS).isNotNull(),
				() -> assertThat(expectedNoteDTOS).hasSize(2)
		);
	}

	@Test
	public void getAllNotes_whenUserHasNoNotes_returnNoNotes() {
		final User suppliedUser = new UserInstanceProvider().withId(3L).getInstanceWithId();
		final Note note1 = new NoteInstanceProvider().withImageName(null).getInstanceWithCreatedBy();
		final Note note2 = new NoteInstanceProvider().withImageName(null).getInstanceWithCreatedBy();
		final Note note3 = new NoteInstanceProvider().withImageName(null).withCreatedBy(2L).getInstanceWithCreatedBy();
		final List<Note> notes = List.of(note1, note2, note3);

		when(noteRepository.findAll()).thenReturn(notes);

		final List<NoteDTO> expectedNoteDTOS = noteService.getAllNotes(suppliedUser.getId());

		assertAll(
				() -> assertThat(expectedNoteDTOS).isNotNull(),
				() -> assertThat(expectedNoteDTOS).hasSize(0)
		);
	}

	@Test
	public void getAllNotes_whenUserHasNotes_returnsAllUserFinishedNotes() {
		final User suppliedUser = new UserInstanceProvider().getInstanceWithId();
		final Note note1 = new NoteInstanceProvider().withImageName(null).getInstanceWithCreatedBy();
		final Note note2 = new NoteInstanceProvider().withImageName(null).withIsFinished(true).getInstanceWithCreatedBy();
		final Note note3 = new NoteInstanceProvider().withImageName(null).withIsFinished(true).getInstanceWithCreatedBy();
		final List<Note> notes = List.of(note1, note2, note3);

		when(noteRepository.findAll()).thenReturn(notes);

		final List<NoteDTO> expectedNoteDTOS = noteService.getAllNotes(suppliedUser.getId(), true);

		assertAll(
				() -> assertThat(expectedNoteDTOS).isNotNull(),
				() -> assertThat(expectedNoteDTOS).hasSize(2)
		);
	}

	@Test
	public void getAllNotes_whenUserHasNotes_returnsAllUserUnfinishedNotes() {
		final User suppliedUser = new UserInstanceProvider().getInstanceWithId();
		final Note note1 = new NoteInstanceProvider().withImageName(null).getInstanceWithCreatedBy();
		final Note note2 = new NoteInstanceProvider().withImageName(null).withIsFinished(true).getInstanceWithCreatedBy();
		final Note note3 = new NoteInstanceProvider().withImageName(null).withIsFinished(true).getInstanceWithCreatedBy();
		final List<Note> notes = List.of(note1, note2, note3);

		when(noteRepository.findAll()).thenReturn(notes);

		final List<NoteDTO> expectedNoteDTOS = noteService.getAllNotes(suppliedUser.getId(), false);

		assertAll(
				() -> assertThat(expectedNoteDTOS).isNotNull(),
				() -> assertThat(expectedNoteDTOS).hasSize(1)
		);
	}

	@Test
	public void getAllNotesForToday_whenUserHasNotesForToday_returnsAllUserNotesForToday() {
		final User suppliedUser = new UserInstanceProvider().getInstanceWithId();
		final Note note1 = new NoteInstanceProvider().withImageName(null).withDueDate(LocalDateTime.of(2000, 01, 01, 0, 0, 0)).getInstanceWithCreatedBy();
		final Note note2 = new NoteInstanceProvider().withImageName(null).withDueDate(LocalDateTime.now()).getInstanceWithCreatedBy();
		final Note note3 = new NoteInstanceProvider().withImageName(null).withDueDate(null).getInstanceWithCreatedBy();
		final Note note4 = new NoteInstanceProvider().withImageName(null).withDueDate(null).withIsFinished(true).getInstanceWithCreatedBy();
		final List<Note> notes = List.of(note1, note2, note3, note4);

		when(noteRepository.findAll()).thenReturn(notes);

		final List<NoteDTO> expectedNoteDTOS = noteService.getNotesForToday(suppliedUser.getId());

		assertAll(
				() -> assertThat(expectedNoteDTOS).isNotNull(),
				() -> assertThat(expectedNoteDTOS).hasSize(1)
		);
	}

	@Test
	public void getAllNotesForToday_whenUserHasNoNotesForToday_returnsNoNotes() {
		final User suppliedUser = new UserInstanceProvider().getInstanceWithId();
		final Note note1 = new NoteInstanceProvider().withImageName(null).withDueDate(LocalDateTime.of(2000, 01, 01, 0, 0, 0)).getInstanceWithCreatedBy();
		final Note note2 = new NoteInstanceProvider().withImageName(null).withDueDate(null).getInstanceWithCreatedBy();
		final Note note3 = new NoteInstanceProvider().withImageName(null).withDueDate(null).withIsFinished(true).getInstanceWithCreatedBy();
		final List<Note> notes = List.of(note1, note2, note3);

		when(noteRepository.findAll()).thenReturn(notes);

		final List<NoteDTO> expectedNoteDTOS = noteService.getNotesForToday(suppliedUser.getId());

		assertAll(
				() -> assertThat(expectedNoteDTOS).isNotNull(),
				() -> assertThat(expectedNoteDTOS).hasSize(0)
		);
	}

	@Test
	public void getNoteById_whenUserHasNote_returnsNote() {
		final User suppliedUser = new UserInstanceProvider().getInstanceWithId();
		final Note note = new NoteInstanceProvider().getInstanceWithCreatedBy();

		when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));

		final NoteDTO expectedNote = noteService.getNoteDTOById(note.getId(), suppliedUser.getId());

		assertAll(
				() -> assertThat(expectedNote).isNotNull(),
				() -> assertThat(expectedNote).isEqualToIgnoringGivenFields(note,
						"image64",
						"active")
		);
	}

	@Test
	public void getNoteById_whenUserHasNoNote_throwsResourceNotFoundException() {
		final Note note = new NoteInstanceProvider().getInstance();

		final User suppliedUser = new UserInstanceProvider().getInstanceWithId();

		when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

		final Exception exceptionThrown = assertThrows(ResourceNotFoundException.class,
				() -> noteService.getNoteDTOById(anyLong(), suppliedUser.getId()));

		assertThat(exceptionThrown).isNotNull();
	}

	@Test
	public void createNewNote_whenNoteIsOk_saveNewNote() {
		final Note savedNote = new NoteInstanceProvider().getInstance();
		final NoteDTO noteDTO = new NoteDTOInstanceProvider().getInstance();

		when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

		NoteDTO expectSavedDTO = noteService.createNote(noteDTO);

		assertAll(
				() -> assertThat(expectSavedDTO).isNotNull(),
				() -> assertThat(expectSavedDTO).isEqualToIgnoringGivenFields(noteDTO,
						"image64")
		);
	}

	@Test
	public void saveNoteByDTO_whenNoteDTOIsOk_saveNewNote() {
		final Note savedNote = new NoteInstanceProvider().withImageName(null).getInstance();
		final NoteDTO noteDTO = new NoteDTOInstanceProvider().getInstance();

		when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

		NoteDTO expectSavedDTO = noteService.saveNoteByDTO(noteDTO);

		assertAll(
				() -> assertThat(expectSavedDTO).isNotNull(),
				() -> assertThat(expectSavedDTO).isEqualToIgnoringGivenFields(noteDTO,
						"imageName",
						"image64")
		);
	}

	@Test
	public void deleteNoteById_whenNoteExists_returnsInactiveNoteDTO() {
		final Note note = new NoteInstanceProvider().getInstance();
		final Note inactiveNote = new NoteInstanceProvider().withIsActive(false).getInstance();

		when(noteRepository.findById(anyLong())).thenReturn(Optional.of(note));
		when(noteRepository.save(any(Note.class))).thenReturn(inactiveNote);

		NoteDTO deletedNote = noteService.deleteNoteById(anyLong());

		assertThat(deletedNote).isNotNull();
	}

	@Test
	public void deleteNoteById_whenNoteNotExists_throwsResourceNotFoundException() {
		final Note note = new NoteInstanceProvider().getInstance();
		final Note inactiveNote = new NoteInstanceProvider().withIsActive(false).getInstance();

		when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

		final Exception exceptionThrown = assertThrows(ResourceNotFoundException.class,
				() -> noteService.deleteNoteById(anyLong()));

		assertThat(exceptionThrown).isNotNull();
	}

	@Test
	public void convertNoteToNoteDtoWithImage_whenNoteIsNull_returnNull() {
		NoteDTO convertedDTO = noteService.convertNoteToNoteDtoWithImage(null);

		assertThat(convertedDTO).isNull();
	}

	@Test
	public void convertNoteToNoteDtoWithImage_whenNoteHasNoImage_returnNoteDTOWithoutImage64() {

		final Note note = new NoteInstanceProvider().withImageName(null).getInstance();

		NoteDTO convertedDTO = noteService.convertNoteToNoteDtoWithImage(note);

		assertAll(
				() -> assertThat(convertedDTO).isNotNull(),
				() -> assertThat(convertedDTO.getImage64()).isEmpty(),
				() -> assertThat(convertedDTO).isEqualToIgnoringGivenFields(note,
						"imageName",
						"image64")
		);
	}
}
