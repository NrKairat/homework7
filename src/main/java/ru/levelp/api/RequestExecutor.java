package ru.levelp.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.InternalError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.levelp.api.entities.AuthPayload;
import ru.levelp.api.entities.RequestContainer;
import ru.levelp.api.entities.ResponseContainer;
import ru.levelp.api.exceptions.RegistrationException;
import ru.levelp.controllers.DaoController;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.Note;

/**
 * Created by кайрат on 01.01.2017.
 */
@Component("requestExecutor")
public class RequestExecutor {
    private Gson gson;
    private DaoController daoController;

    @Autowired
    public RequestExecutor(Gson gson, DaoController daoController) {
        this.gson = gson;
        this.daoController = daoController;
    }

    public ResponseContainer execute(String json, String method) throws RegistrationException {


        switch (method){
            case Method.AUTHORIZE:
                RequestContainer<AuthPayload> request =
                        gson.fromJson(json, new TypeToken<RequestContainer<AuthPayload>>(){}.getType());
                return daoController.authorize(request.getPayload().getEmail(),
                        request.getPayload().getPwdHash());

            case Method.REGISTRATION:
                RequestContainer<AuthPayload> requestReg =
                        gson.fromJson(json, new TypeToken<RequestContainer<AuthPayload>>(){}.getType());
                return daoController.registration(requestReg.getPayload().getEmail(),
                        requestReg.getPayload().getPwdHash(),requestReg.getPayload().getName());

            case Method.GET_MY_NOTES:
                RequestContainer<String> requestMyN =
                        gson.fromJson(json, new TypeToken<RequestContainer<String>>(){}.getType());
                return daoController.getNotes(requestMyN.getPayload());

            case Method.ADD_NOTE:

                RequestContainer<Note> requestNote =
                        gson.fromJson(json, new TypeToken<RequestContainer<Note>>(){}.getType());
                Note note = (Note)requestNote.getPayload();
                return daoController.addNote(note);

            case Method.GET_NOTE:
                RequestContainer<String> requestIdNote =
                        gson.fromJson(json, new TypeToken<RequestContainer<String>>(){}.getType());
                return  daoController.getNote(requestIdNote.getPayload());

            case Method.DELETE_NOTE:
                RequestContainer<String> requestIdNoteD =
                        gson.fromJson(json, new TypeToken<RequestContainer<String>>(){}.getType());
                return  daoController.deleteNote(requestIdNoteD.getPayload());

            case Method.EDIT_NOTE:
                RequestContainer<Note> requestIdNoteE =
                        gson.fromJson(json, new TypeToken<RequestContainer<Note>>(){}.getType());
                return  daoController.editNote(requestIdNoteE);

            case Method.GET_USER:
                RequestContainer<String > requestG =
                        gson.fromJson(json, new TypeToken<RequestContainer<String>>(){}.getType());
                return daoController.getUser(requestG.getPayload());

            case Method.GET_ALL_USERS:
                RequestContainer<String > requestAU =
                        gson.fromJson(json, new TypeToken<RequestContainer<String>>(){}.getType());
                return daoController.getAllUsers();

            case Method.ADD_ACCESS_RIGHT:
                RequestContainer<AccessRight> requestAAR =
                        gson.fromJson(json, new TypeToken<RequestContainer<AccessRight>>(){}.getType());
                return daoController.addAccessRight(requestAAR.getPayload());

            case Method.REMOVE_ACCESS_RIGHT:
                RequestContainer<AccessRight> requestRAR =
                        gson.fromJson(json, new TypeToken<RequestContainer<AccessRight>>(){}.getType());
                return daoController.removeAccessRight(requestRAR.getPayload());


        }
        throw new InternalError("INTERNAL_SERVER_ERROR");
    }
}
