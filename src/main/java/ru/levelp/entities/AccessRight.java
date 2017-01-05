package ru.levelp.entities;

import javax.persistence.Column;
//hibernate
@javax.persistence.Entity
@javax.persistence.Table(name = "note_rights")
//morphia
@org.mongodb.morphia.annotations.Entity("access_rights")
public class AccessRight implements BaseEntity<String>{
    @Column(name="id")
    @javax.persistence.Id
    //morphia
    @org.mongodb.morphia.annotations.Id
    private String id;
    @Column(name = "mode")
    private int mode;
    @Column(name = "userId")
    private String userId;
    @Column(name = "noteId")
    private String noteId;

    public AccessRight(){
    }

    public AccessRight(int mode, String userId){
        this.mode = mode;
        this.userId = userId;
    }

    public AccessRight(int mode, String userId, String noteId) {
        this.mode = mode;
        this.userId = userId;
        this.noteId = noteId;
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof AccessRight))return false;
        AccessRight otherAccessRight = (AccessRight) other;

        if(otherAccessRight.getMode() == this.mode &&
                otherAccessRight.getUserId().equals(this.userId)){
            return true;
        }

        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
