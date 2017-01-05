package ru.levelp.controllers;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.InternalError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.levelp.api.entities.RequestContainer;
import ru.levelp.api.entities.ResponseContainer;
import ru.levelp.api.exceptions.RegistrationException;
import ru.levelp.api.exceptions.TokenException;
import ru.levelp.dao.accessRight.AccessRightServiceMongo;
import ru.levelp.dao.note.NoteDAO;
import ru.levelp.dao.user.UserDAO;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.Note;
import ru.levelp.entities.User;

import java.util.List;
import java.util.UUID;

@Controller("daoController")
public class DaoController {
    private UserDAO userServiceMongo;
    private NoteDAO noteServiceMongo;
    private AccessRightServiceMongo accessRightServiceMongo;
    private String token;

    @Autowired
    public DaoController(UserDAO userServiceMongo, NoteDAO noteServiceMongo, AccessRightServiceMongo accessRightServiceMongo) {
        this.userServiceMongo = userServiceMongo;
        this.noteServiceMongo = noteServiceMongo;
        this.accessRightServiceMongo = accessRightServiceMongo;
    }



    public ResponseContainer<String > authorize(String email, String pwdHash) {
        User user = userServiceMongo.getByEmail(email);
        if (user != null && user.getPwdHash().equals(pwdHash)) {
            token = UUID.randomUUID().toString()+UUID.randomUUID().toString();
            user.setToken(token);
            userServiceMongo.update(user);
            ResponseContainer<String> responseContainer = new ResponseContainer<>();
            responseContainer.setPayload(user.getToken());
            return responseContainer;
        }
        throw new InternalError("");
    }

    public ResponseContainer<String > registration(String email, String pwdHash, String name) throws RegistrationException {
        User user = userServiceMongo.getByEmail(email);
        if (user == null) {
            token = UUID.randomUUID().toString()+UUID.randomUUID().toString();
            user = new User(UUID.randomUUID().toString(),email,pwdHash,name,token);
            userServiceMongo.add(user);
            ResponseContainer<String> responseContainer = new ResponseContainer<>();
            responseContainer.setPayload(user.getToken());
            return responseContainer;
        }
        throw new RegistrationException("");
    }

    public ResponseContainer<List<Note> > getNotes(String email)  {
            List<Note> notes = noteServiceMongo.getForUser(email);
            ResponseContainer<List<Note> > responseContainer = new ResponseContainer<>();
            responseContainer.setPayload(notes);
            return responseContainer;
    }

    public ResponseContainer<String> addNote(Note note)  {
        noteServiceMongo.add(note);
        ResponseContainer<String > responseContainer = new ResponseContainer<>();
        responseContainer.setPayload("");
        return responseContainer;
    }

    public ResponseContainer<Note> getNote(String id)  {
        Note note = noteServiceMongo.get(id);
        ResponseContainer<Note> responseContainerN = new ResponseContainer<Note>();
        responseContainerN.setPayload(note);
        return responseContainerN;
    }

    public ResponseContainer deleteNote(String idNote) {
        noteServiceMongo.delete(idNote);
        ResponseContainer<Note> responseContainer = new ResponseContainer<Note>();
        return responseContainer;
    }

    public ResponseContainer editNote(RequestContainer<Note> container) {
        Note note = container.getPayload();
        noteServiceMongo.update(note);
        ResponseContainer<Note> responseContainer = new ResponseContainer<Note>();
        return responseContainer;
    }

    public ResponseContainer getUser(String email) {
        User user = userServiceMongo.getByEmail(email);
        ResponseContainer<User> responseContainer = new ResponseContainer<User>();
        responseContainer.setPayload(user);
        return responseContainer;
    }

    public ResponseContainer getAllUsers() {
        List<User> userList = userServiceMongo.getAll();
        ResponseContainer<List<User>> responseContainer = new ResponseContainer<List<User>>();
        responseContainer.setPayload(userList);
        return responseContainer;
    }

    public ResponseContainer addAccessRight(AccessRight accessRight) {
        accessRightServiceMongo.addAccessRight(accessRight);
        ResponseContainer<AccessRight> responseContainer = new ResponseContainer<AccessRight>();
        return responseContainer;
    }


    public ResponseContainer removeAccessRight(AccessRight accessRight) {
        accessRightServiceMongo.removeAccessRight(accessRight);
        ResponseContainer<AccessRight> responseContainer = new ResponseContainer<AccessRight>();
        return responseContainer;
    }
}
