package ru.levelp.api.entities;

/**
 * Created by кайрат on 01.01.2017.
 */
public class AuthPayload {
    private String email;
    private String pwdHash;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwdHash(String pwdHash) {
        this.pwdHash = pwdHash;
    }

    public void setName(String name) {
        this.name = name;
    }
}
