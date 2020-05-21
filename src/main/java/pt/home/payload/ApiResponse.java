package pt.home.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    //General Api response

    private Boolean success;
    private String message;
}