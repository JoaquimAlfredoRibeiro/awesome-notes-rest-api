package pt.home.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {

    private Long id;

    @NotBlank(message = "enter_note")
    private String content;

    private Boolean finished;

    private String imageName;

    private LocalDateTime dueDate;

    //String for encoded image
    private String image64;
}
