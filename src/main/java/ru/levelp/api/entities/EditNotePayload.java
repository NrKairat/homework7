package ru.levelp.api.entities;

/**
 * Created by Windows on 03.03.2017.
 */
import com.google.gson.annotations.Expose;

public class EditNotePayload {
    @Expose
    private String noteId;
    @Expose
    private String title;
    @Expose
    private String body;

    public String getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
