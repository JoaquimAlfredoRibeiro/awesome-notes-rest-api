package pt.home.services;

import pt.home.api.v1.model.NoteDTO;

import java.time.LocalDateTime;

//static factory
public class NoteDTOStaticFactory {

    private NoteDTOStaticFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static NoteDTO getInstance(
            final long id,
            final String content,
            final boolean isFinished,
            final String imageName,
            final LocalDateTime dueDate,
            final String image64
    ) {
        return new NoteDTO(id, content, isFinished, imageName, dueDate, image64);
    }
}