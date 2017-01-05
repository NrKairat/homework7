package ru.levelp.entities;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

import javax.persistence.*;

//hibernate
@javax.persistence.Entity
//morphia
@org.mongodb.morphia.annotations.Entity("notes")
//hibernate
@Table(name = "notes")
public class Note implements BaseEntity<String> {
    //hibernate
    @Expose
    @Column(name="id")
    @javax.persistence.Id
    //morphia
    @org.mongodb.morphia.annotations.Id
    private String id;
    @Expose
    @Column(name = "title") //hib
    private String title;
    @Expose
    @Column(name = "body") //hib
    private String body;
    @Expose
    @Column(name = "created") //hib
    private long created;
    @Expose
    @Column(name = "updated") //hib
    private long updated;
    @Expose
    @Column(name = "author") //hib
    private String author;

    public Note() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
