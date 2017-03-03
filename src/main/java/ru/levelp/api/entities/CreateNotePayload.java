package ru.levelp.api.entities;

import com.google.gson.annotations.Expose;

public class CreateNotePayload {
    @Expose
    private String title;
    @Expose
    private String body;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
