package ru.levelp;

import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.levelp.api.WSHandler;
import ru.levelp.api.exceptions.ProtocolException;
import ru.levelp.api.exceptions.RegistrationException;
import ru.levelp.api.exceptions.TokenValidatorException;
import ru.levelp.dao.user.UserServiceMongo;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws TokenValidatorException, ProtocolException, RegistrationException {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        WSHandler wsHandler = (WSHandler)context.getBean("wsHandler");
        Gson gson = (Gson)context.getBean("gson");
        wsHandler.onRequestReceived("jfhfhm");


    }
}
