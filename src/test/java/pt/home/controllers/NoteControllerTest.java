package pt.home.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.home.api.v1.model.NoteDTO;
import pt.home.controllers.v1.NoteController;
import pt.home.exceptions.ResourceNotFoundException;
import pt.home.instanceprovers.NoteDTOInstanceProvider;
import pt.home.services.NoteService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pt.home.controllers.AbstractRestControllerTest.asJsonString;

public class NoteControllerTest {

    @Mock
    NoteService noteService;

    @InjectMocks
    NoteController noteController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getAllNotes_whenUserHasNotes_returnListNoteDTO() throws Exception {
        NoteDTO noteDTO1 = new NoteDTOInstanceProvider().getInstance();
        NoteDTO noteDTO2 = new NoteDTOInstanceProvider().getInstance();
        List<NoteDTO> noteDTOS = List.of(noteDTO1, noteDTO2);

        when(noteService.getAllNotes(any())).thenReturn(noteDTOS);

        mockMvc.perform(get(NoteController.BASE_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAllNotes_whenUserHasNoNotes_returnEmptyList() throws Exception {
        List<NoteDTO> noteDTOS = List.of();

        when(noteService.getAllNotes(any())).thenReturn(noteDTOS);

        mockMvc.perform(get(NoteController.BASE_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getAllNotesByFinishedTrue_whenUserHasNotes_returnListNoteDTO() throws Exception {
        NoteDTO noteDTO1 = new NoteDTOInstanceProvider().withIsFinished(true).getInstance();
        NoteDTO noteDTO2 = new NoteDTOInstanceProvider().withIsFinished(true).getInstance();
        List<NoteDTO> noteDTOS = List.of(noteDTO1, noteDTO2);

        when(noteService.getAllNotes(any(), anyBoolean())).thenReturn(noteDTOS);

        mockMvc.perform(get(NoteController.BASE_URL + "/all/true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAllNotesByFinishedFalse_whenUserHasNotes_returnListNoteDTO() throws Exception {
        NoteDTO noteDTO1 = new NoteDTOInstanceProvider().getInstance();
        NoteDTO noteDTO2 = new NoteDTOInstanceProvider().getInstance();
        List<NoteDTO> noteDTOS = List.of(noteDTO1, noteDTO2);

        when(noteService.getAllNotes(any(), anyBoolean())).thenReturn(noteDTOS);

        mockMvc.perform(get(NoteController.BASE_URL + "/all/false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAllNotesForToday_whenUserHasNotes_returnListNoteDTO() throws Exception {
        NoteDTO noteDTO1 = new NoteDTOInstanceProvider().getInstance();
        NoteDTO noteDTO2 = new NoteDTOInstanceProvider().getInstance();
        List<NoteDTO> noteDTOS = List.of(noteDTO1, noteDTO2);

        when(noteService.getNotesForToday(any())).thenReturn(noteDTOS);

        mockMvc.perform(get(NoteController.BASE_URL + "/all/today")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getNoteById_whenUserHasNote_returnsNoteDTO() throws Exception {
        NoteDTO noteDTO1 = new NoteDTOInstanceProvider().getInstance();

        when(noteService.getNoteDTOById(anyLong(), any())).thenReturn(noteDTO1);

        //then
        mockMvc.perform(get(NoteController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(NoteDTOInstanceProvider.BASE_ID))
                .andExpect(jsonPath("$.content").value(NoteDTOInstanceProvider.CONTENT))
                .andExpect(jsonPath("$.imageName").value(NoteDTOInstanceProvider.IMAGE_NAME))
                .andExpect(jsonPath("$.finished").value(Boolean.FALSE));
    }

    @Test
    public void getNoteById_whenUserHasNoNote_throwsResourceNotFoundException() throws Exception {
        NoteDTO noteDTO1 = new NoteDTOInstanceProvider().getInstance();

        when(noteService.getNoteDTOById(anyLong(), any())).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get(NoteController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createNote_noteDTOIsOK_returnNoteDTO() throws Exception {
        NoteDTO noteDTO = new NoteDTOInstanceProvider().withDueDate(null).getInstance();

        when(noteService.createNote(any(NoteDTO.class))).thenReturn(noteDTO);

        //then
        mockMvc.perform(post(NoteController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(NoteDTOInstanceProvider.BASE_ID))
                .andExpect(jsonPath("$.content").value(NoteDTOInstanceProvider.CONTENT))
                .andExpect(jsonPath("$.imageName").value(NoteDTOInstanceProvider.IMAGE_NAME))
                .andExpect(jsonPath("$.finished").value(Boolean.FALSE));
    }

    @Test
    public void createNote_noteDTOIHasNoContent_returnNoteDTO() throws Exception {
        NoteDTO noteDTO = new NoteDTOInstanceProvider().withContent(null).withDueDate(null).getInstance();

        //then
        mockMvc.perform(post(NoteController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.content", equalTo("enter_note")));
    }

    @Test
    public void createNote_noteDTOIContentIsBlank_returnNoteDTO() throws Exception {
        NoteDTO noteDTO = new NoteDTOInstanceProvider().withContent("").withDueDate(null).getInstance();

        //then
        mockMvc.perform(post(NoteController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.content", equalTo("enter_note")));
    }

    @Test
    public void updateNote_noteDTOisOK_returnNoteDTO() throws Exception {
        NoteDTO noteDTO = new NoteDTOInstanceProvider().withDueDate(null).getInstance();
        NoteDTO returnDTO = new NoteDTOInstanceProvider().withDueDate(null).getInstance();

        when(noteService.saveNoteByDTO(any(NoteDTO.class))).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(put(NoteController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(noteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(NoteDTOInstanceProvider.BASE_ID))
                .andExpect(jsonPath("$.content").value(NoteDTOInstanceProvider.CONTENT))
                .andExpect(jsonPath("$.imageName").value(NoteDTOInstanceProvider.IMAGE_NAME))
                .andExpect(jsonPath("$.finished").value(Boolean.FALSE));
    }

    @Test
    public void deleteNote_whenCalled_callsService() throws Exception {
        mockMvc.perform(delete(NoteController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(noteService).deleteNoteById(anyLong());
    }
}
