package pt.home.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

//returns object as json string
public abstract class AbstractRestControllerTest {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
