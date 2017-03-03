package ru.levelp.api.entities;

/**
 * Created by Windows on 03.03.2017.
 */
import com.google.gson.annotations.Expose;

public class RegisterPayload {
    @Expose
    private String email;
    @Expose
    private String pwdHash;
    @Expose
    private String name;

    public String getEmail() {
        return email;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public String getName() {
        return name;
    }
}
