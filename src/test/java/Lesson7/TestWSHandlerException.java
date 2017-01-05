package Lesson7;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.levelp.AppConfig;
import ru.levelp.api.Method;
import ru.levelp.api.WSHandler;

import ru.levelp.api.entities.AuthPayload;
import ru.levelp.api.entities.RequestContainer;
import ru.levelp.api.exceptions.ProtocolException;
import ru.levelp.api.exceptions.RegistrationException;
import ru.levelp.api.exceptions.TokenValidatorException;
import ru.levelp.dao.user.UserDAO;
import ru.levelp.dao.user.UserServiceMongo;
import ru.levelp.entities.User;

/**
 * ДЗ - "Протестировать каждый этап в цепочке"
 * Тестирование метода WSHandler.onRequestReceived на проброс исключений на всех его этапах
 */
public class TestWSHandlerException {


    @Before
    public void fillMongoDB(){
        //Проверяем бины
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //Создаем пользователя  : (id, email, pwdHash, name, token)
        User user = new User("0","Ivan@mail.ru","12345","Ivan",null);
        UserDAO userServiceMongo = (UserServiceMongo)context.getBean("userServiceMongo");
        //Добавляем пользователя в базу
        userServiceMongo.add(user);

    }

    //Проверка исключения JsonSyntaxException
    @Test(expected = JsonSyntaxException.class)
    public void testJsonRequestException() throws JsonSyntaxException, ProtocolException, TokenValidatorException, RegistrationException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        WSHandler wsHandler = (WSHandler)context.getBean("wsHandler");
        //Посылаем некорректные данные вместо структуры json-а
        wsHandler.onRequestReceived("wgervdgwef");
    }


    //Проверка исключения ProtocolException
    @Test(expected = ProtocolException.class)
    public void testProtocolExeption() throws TokenValidatorException, ProtocolException, RegistrationException {

        String userEmail = "Ivan@mail.ru";
        String userPwdHash = "12345";

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        RequestContainer<AuthPayload> requestContainer = new RequestContainer<AuthPayload>();

        //Заведомо некорректное поле requestId
        requestContainer.setRequestId(null);
        //Заведомо некорректное поле method
        requestContainer.setMethod(null);
        requestContainer.setToken(null);

        //Создаем класс в ктором будут храниться пароль и "email" пользователя
        AuthPayload authPayload = new AuthPayload();
        authPayload.setEmail(userEmail);
        authPayload.setPwdHash(userPwdHash);
        requestContainer.setPayload(authPayload);


        Gson gson = (Gson)context.getBean("gson");
        String jsonRequest = gson.toJson(requestContainer);


        WSHandler wsHandler = (WSHandler)context.getBean("wsHandler");


        wsHandler.onRequestReceived(jsonRequest);

    }

    //Проверка исключения TokenValidatorException
    @Test(expected = TokenValidatorException.class)
    public void testTokenValidatorException()
            throws JsonSyntaxException, TokenValidatorException, ProtocolException, RegistrationException {

        String userEmail = "Ivan@mail.ru";
        String userPwdHash = "12345";

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        RequestContainer<AuthPayload> requestContainer = new RequestContainer<AuthPayload>();

        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.GET_MY_NOTES);
        //В метод требующий токена отправляем пустой токен
        requestContainer.setToken(null);

        //Создаем класс в ктором будут храниться пароль и "email" пользователя
        AuthPayload authPayload = new AuthPayload();
        authPayload.setEmail(userEmail);
        authPayload.setPwdHash(userPwdHash);
        requestContainer.setPayload(authPayload);


        Gson gson = (Gson)context.getBean("gson");
        String jsonRequest = gson.toJson(requestContainer);


        WSHandler wsHandler = (WSHandler)context.getBean("wsHandler");


        wsHandler.onRequestReceived(jsonRequest);

    }



}
