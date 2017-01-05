package Lesson7;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.levelp.AppConfig;
import ru.levelp.api.Method;
import ru.levelp.api.WSHandler;
import ru.levelp.api.entities.AuthPayload;
import ru.levelp.api.entities.RequestContainer;
import ru.levelp.api.entities.ResponseContainer;
import ru.levelp.api.exceptions.ProtocolException;
import ru.levelp.api.exceptions.RegistrationException;
import ru.levelp.api.exceptions.TokenValidatorException;
import ru.levelp.dao.accessRight.AccessRightServiceMongo;
import ru.levelp.dao.note.NoteServiceMongo;
import ru.levelp.dao.user.UserDAO;
import ru.levelp.dao.user.UserServiceMongo;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.Note;
import ru.levelp.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Тестирование методов работы с клиентом(DaoController).
 * В методе wsHandler.onRequestReceived изменено возвращаемое значение, т.к. клиента
 * нет, из этого метода будем получать ответ.
 */
public class TestDaoController {
    //Собираем бины
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);;
    Gson gson = (Gson)context.getBean("gson");
    WSHandler wsHandler = (WSHandler)context.getBean("wsHandler");
    //Общие поля для класса
    String userEmail = "Ivan999@mail.ru";
    String userPwdHash = "12345";
    String userName ="Ivan";
    String token;
    String otherToken;
    String otherEmail;
    String idNote;
    String titleNote;
    String bodyNote;
    long createdNote;
    long updatedNote;


    @Test
    //Проверка метода регистрации пользователя (Method.REGISTRATION)
    public void testRegistration() throws TokenValidatorException, ProtocolException, RegistrationException {

        //Очищаем базу данных Монго, т.е. все 3 таблицы
        UserServiceMongo userDB = (UserServiceMongo)context.getBean("userServiceMongo");
        //очищаем таблицу
        userDB.drop();
        NoteServiceMongo noteDB = (NoteServiceMongo)context.getBean(("noteServiceMongo"));
        noteDB.drop();
        AccessRightServiceMongo accessDB = (AccessRightServiceMongo)context.getBean("accessRightServiceMongo");
        accessDB.drop();

        //Создаем объект AuthPayload в ктором будут храниться пароль и "email" пользователя
        AuthPayload authPayload = new AuthPayload();
        authPayload.setEmail(userEmail);
        authPayload.setPwdHash(userPwdHash);
        authPayload.setName(userName);
        //Создаем запрос с методом "авторизация" и пустым токеном
        RequestContainer<AuthPayload> requestContainer = new RequestContainer<AuthPayload>();
        requestContainer.setPayload(authPayload);
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.REGISTRATION);
        //токен в этом методе не нужен
        requestContainer.setToken(null);
        //парсим в json наш RequestContainer
        String jsonRequest = gson.toJson(requestContainer);
        //отправляем запрос на сервер
        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        //Проверяем что ответ от метода onRequestReceived не равен null
        Assert.assertNotNull(jsonResponse);
    }

    @Test
    //Проверка авторизации пользователя
    public void testAuthorize() throws TokenValidatorException, ProtocolException, RegistrationException {
        //Сначало необходимо запустить метод testRegistration, для создания пользователя
        testRegistration();
        //Создаем класс в ктором будут храниться пароль и "email" пользователя
        AuthPayload authPayload = new AuthPayload();
        authPayload.setEmail(userEmail);
        authPayload.setPwdHash(userPwdHash);
        //Создаем запрос с методом "авторизация" и пустым токеном
        RequestContainer<AuthPayload> requestContainer = new RequestContainer<AuthPayload>();
        requestContainer.setPayload(authPayload);
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.AUTHORIZE);
        //Для авторизации тоже не нужен токен
        requestContainer.setToken(null);

        String jsonRequest = gson.toJson(requestContainer);

        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        ResponseContainer container = gson.fromJson(jsonResponse,ResponseContainer.class);
        //Сохраняем наш токен в переменной класса, т.к. в остальных методах необходимо знать токен
        token = (String) container.getPayload();
        //Проверяем что нам пришел токен
        Assert.assertNotNull(token);
    }

    @Test
    //Проверка получения пользователя
    public void testGetUser() throws ProtocolException, RegistrationException, TokenValidatorException{
        //Регистрируем и авторизуем пользователя
        testRegistration();
        testAuthorize();

        //Получаем пользователя по Email
        RequestContainer<String> requestContainer = new RequestContainer<String>();
        requestContainer.setPayload(userEmail);
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.GET_USER);
        requestContainer.setToken(token);

        String jsonRequest = gson.toJson(requestContainer);

        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<User> container =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<User>>(){}.getType());


        User user = (User) container.getPayload();
        //Сравниваем поля полученного пользователя с сохраненными
        Assert.assertEquals(userEmail,user.getEmail());
        Assert.assertEquals(userName,user.getName());

    }
    @Test
    //Тестирование метода получения всех пользователей. В этом методе помимо зарегистрированного пользователя
    // создаем в цикле еще 6 пользователей.
    public void testGetAllUsers() throws ProtocolException, RegistrationException, TokenValidatorException{
        testRegistration();
        testAuthorize();

        ArrayList<AuthPayload> authList = new ArrayList<AuthPayload>();

        //В цикле создаем 6 пользователей
        for(int i=0;i<6;i++){
            //Создаем класс в ктором будут храниться пароль и "email" пользователя
            AuthPayload authPayload = new AuthPayload();
            authPayload.setEmail("box"+i+"@mail.ru");
            authPayload.setPwdHash("12345");
            authPayload.setName("Name"+i);
            authList.add(authPayload);
            //Создаем запрос с методом "авторизация" и пустым токеном
            RequestContainer<AuthPayload> requestContainer = new RequestContainer<AuthPayload>();
            requestContainer.setPayload(authPayload);
            requestContainer.setRequestId("1");
            requestContainer.setMethod(Method.REGISTRATION);
            requestContainer.setToken(null);

            String jsonRequest = gson.toJson(requestContainer);

            wsHandler.onRequestReceived (jsonRequest);
        }


        //Получаем всех пользователей
        RequestContainer<String> requestContainer = new RequestContainer<String>();
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.GET_ALL_USERS);
        requestContainer.setToken(token);

        String jsonRequest = gson.toJson(requestContainer);

        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<List<User>> container =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<List<User>>>(){}.getType());


        List<User> userList = (List<User>) container.getPayload();

        //Перебираем полученную коллекцию и проверяем поля id, email

        Assert.assertEquals(userEmail,userList.get(0).getEmail());
        Assert.assertEquals(userName,userList.get(0).getName());

        for(int i=0;i<userList.size()-1;i++){
            Assert.assertEquals(authList.get(i).getEmail(),userList.get(i+1).getEmail());
            Assert.assertEquals(authList.get(i).getName(),userList.get(i+1).getName());
        }



    }

    @Test
    // добавление заметки
    public void testAddNote() throws ProtocolException, RegistrationException, TokenValidatorException {
        testRegistration();
        testAuthorize();
        idNote = "1";
        titleNote ="TestNote";
        createdNote = System.currentTimeMillis();
        updatedNote = createdNote;
        bodyNote = "Body of test note";

        Note note = new Note();
        note.setId(idNote);
        note.setTitle(titleNote);
        note.setAuthor(userEmail);
        note.setCreated(createdNote);
        note.setUpdated(updatedNote);
        note.setBody(bodyNote);

        RequestContainer<Note> requestContainer = new RequestContainer<Note>();
        requestContainer.setPayload(note);
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.ADD_NOTE);
        requestContainer.setToken(token);

        String jsonRequest = gson.toJson(requestContainer);

        wsHandler.onRequestReceived (jsonRequest);

    }

    @Test
    //Проверка метода получения заметки. Сначало добавляем заметку затем ее же получаем
    public void testGetNote() throws ProtocolException, RegistrationException, TokenValidatorException{
        testRegistration();
        testAuthorize();
        testAddNote();
        //получения заметки
        RequestContainer<String> requestContainer = new RequestContainer<String>();
        requestContainer.setPayload(idNote);
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.GET_NOTE);
        requestContainer.setToken(token);

        String jsonRequest = gson.toJson(requestContainer);

        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<Note> container =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<Note>>(){}.getType());


        Note note = (Note) container.getPayload();

        //Проверка полей полученной заметки с ожидаемыми
        Assert.assertEquals(idNote,note.getId());
        Assert.assertEquals(titleNote,note.getTitle());
        Assert.assertEquals(userEmail,note.getAuthor());
        Assert.assertEquals(bodyNote,note.getBody());
        Assert.assertEquals(createdNote,note.getCreated());
        Assert.assertEquals(updatedNote,note.getUpdated());
    }

    @Test
    //Проверка метода удаления заметки. Добавляем заметку, удаляем ее, получаем заметку по Id, проверяем
    // что заметка удалена и вместо заметки мы получаем null
    public void testDeleteNote() throws ProtocolException, RegistrationException, TokenValidatorException{
        testRegistration();
        testAuthorize();
        testAddNote();

        //Удаление заметки
        RequestContainer<String> requestContainer = new RequestContainer<String>();
        requestContainer.setPayload(idNote);
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.DELETE_NOTE);
        requestContainer.setToken(token);

        String jsonRequest = gson.toJson(requestContainer);

        wsHandler.onRequestReceived (jsonRequest);

        //Попытка получить заметку по id
        requestContainer = new RequestContainer<String>();
        requestContainer.setPayload(idNote);
        requestContainer.setRequestId("1");
        requestContainer.setMethod(Method.GET_NOTE);
        requestContainer.setToken(token);

        jsonRequest = gson.toJson(requestContainer);

        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<Note> container =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<Note>>(){}.getType());


        Note note = (Note) container.getPayload();

        //Проверка получения пустой заметки
        Assert.assertNull(note);
    }

    @Test
    //Проверка метода редактирования заметки. Добавляем заметку, получаем ее , изменяем поля, вызываем метод
    //EDIT_NOTE с измененной заметкой. Получаем заного заметку с измененными полями, проверяем их
    public void testEditNote() throws ProtocolException, RegistrationException, TokenValidatorException{
        testRegistration();
        testAuthorize();
        testAddNote();

        //Поучаем заметку
        RequestContainer<String> requestGetNote = new RequestContainer<String>();
        requestGetNote.setPayload(idNote);
        requestGetNote.setRequestId("1");
        requestGetNote.setMethod(Method.GET_NOTE);
        requestGetNote.setToken(token);

        String jsonRequest = gson.toJson(requestGetNote);

        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<Note> container =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<Note>>(){}.getType());


        Note note = (Note) container.getPayload();

        Assert.assertEquals(idNote,note.getId());
        Assert.assertEquals(titleNote,note.getTitle());
        Assert.assertEquals(userEmail,note.getAuthor());
        Assert.assertEquals(bodyNote,note.getBody());
        Assert.assertEquals(createdNote,note.getCreated());
        Assert.assertEquals(updatedNote,note.getUpdated());

        //Редактируем заметку
        String changeTitle = "changeTitle";
        String changeBody = "changeBody";
        long changeCreated = createdNote+1;
        long changeUpdated = updatedNote+1;

        note.setTitle(changeTitle);
        note.setCreated(changeCreated);
        note.setUpdated(changeUpdated);
        note.setBody(changeBody);

        RequestContainer<Note> requestChange = new RequestContainer<Note>();
        requestChange.setPayload(note);
        requestChange.setRequestId("1");
        requestChange.setMethod(Method.EDIT_NOTE);
        requestChange.setToken(token);

        jsonRequest = gson.toJson(requestChange);

        wsHandler.onRequestReceived (jsonRequest);

        //Отправляем запрос requestGetNote с методом получения заметки
        jsonRequest = gson.toJson(requestGetNote);

        jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        container =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<Note>>(){}.getType());


        note = (Note) container.getPayload();

        //Проверяем что поля заметки изменены
        Assert.assertEquals(idNote,note.getId());
        Assert.assertEquals(changeTitle,note.getTitle());
        Assert.assertEquals(userEmail,note.getAuthor());
        Assert.assertEquals(changeBody,note.getBody());
        Assert.assertEquals(changeCreated,note.getCreated());
        Assert.assertEquals(changeUpdated,note.getUpdated());


    }

    @Test
    //Проверка метода getMyNotes. Создаем 5 заметок пользователя, и 5 заметок для другого пользователя
    //Проверяем что метод getMyNotes возвратил заметки первого пользователя в количестве 5 штук
    public void testGetMyNotes() throws ProtocolException, RegistrationException, TokenValidatorException{
        testRegistration();
        testAuthorize();

        //Создаем заметки для 1-го пользователя
        ArrayList<Note> notes = new ArrayList<>();
        for(int i=0;i<5;i++){
            Note note = new Note();
            note.setId(i+"");
            note.setTitle(titleNote);
            note.setAuthor(userEmail);
            note.setCreated(createdNote);
            note.setUpdated(updatedNote);
            note.setBody(bodyNote);
            notes.add(note);

            RequestContainer<Note> requestContainer = new RequestContainer<Note>();
            requestContainer.setPayload(note);
            requestContainer.setRequestId(""+i);
            requestContainer.setMethod(Method.ADD_NOTE);
            requestContainer.setToken(token);

            String jsonRequest = gson.toJson(requestContainer);

            wsHandler.onRequestReceived (jsonRequest);
        }

        //Создаем заметки для 2-го пользователя
        for(int i=5;i<10;i++){
            Note note = new Note();
            note.setId(i+"");
            note.setTitle(titleNote);
            note.setAuthor("otherEmail");
            note.setCreated(createdNote);
            note.setUpdated(updatedNote);
            note.setBody(bodyNote);

            RequestContainer<Note> requestContainer = new RequestContainer<Note>();
            requestContainer.setPayload(note);
            requestContainer.setRequestId(""+i);
            requestContainer.setMethod(Method.ADD_NOTE);
            requestContainer.setToken(token);

            String jsonRequest = gson.toJson(requestContainer);

            wsHandler.onRequestReceived (jsonRequest);
        }

        //Вызываем метод getMyNotes для 1-го пользователя
        RequestContainer<String> reqCont = new RequestContainer<String>();
        reqCont.setPayload(userEmail);
        reqCont.setRequestId("10");
        reqCont.setMethod(Method.GET_MY_NOTES);
        reqCont.setToken(token);

        String jsonRequest = gson.toJson(reqCont);

        wsHandler.onRequestReceived (jsonRequest);
        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<List<Note>> container =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<List<Note>>>(){}.getType());


        List<Note> getNotes = (List<Note>) container.getPayload();

        //Проверяем количество полученных заметок
        Assert.assertEquals(notes.size(),getNotes.size());

    }

    @Test
    //Проверка добавления прав на заметку.
    public void testAddAccessRight() throws ProtocolException, RegistrationException, TokenValidatorException{

        /*
        * Регистрируем 1 и 2-го пользователя. 1-й пользователь создает 5 заметок и дает права на них 2-му пользователю
        * */
        otherEmail = "otherEmail";

        testRegistration();
        testAuthorize();

        //Регистрируем 2-го пользователя
        //Создаем класс в ктором будут храниться пароль и "email" пользователя
        AuthPayload authPayload = new AuthPayload();
        authPayload.setEmail(otherEmail);
        authPayload.setPwdHash(userPwdHash);
        authPayload.setName(userName);
        //Создаем запрос с методом "авторизация" и пустым токеном
        RequestContainer<AuthPayload> registContainer = new RequestContainer<AuthPayload>();
        registContainer.setPayload(authPayload);
        registContainer.setRequestId("1");
        registContainer.setMethod(Method.REGISTRATION);
        registContainer.setToken(null);
        String jsonRequest = gson.toJson(registContainer);

        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        ResponseContainer container = gson.fromJson(jsonResponse,ResponseContainer.class);
        otherToken = (String) container.getPayload();

        ArrayList<Note> notes = new ArrayList<>();
        //Создаем заметки для 1-го пользователя и даем права на них 2-му пользователю
        for(int i=0;i<5;i++){
            Note note = new Note();
            idNote =i+"";
            note.setId(idNote);
            note.setTitle(titleNote);
            note.setAuthor(userEmail);
            note.setCreated(createdNote);
            note.setUpdated(updatedNote);
            note.setBody(bodyNote);
            notes.add(note);

            RequestContainer<Note> requestContainer = new RequestContainer<Note>();
            requestContainer.setPayload(note);
            requestContainer.setRequestId(""+i);
            requestContainer.setMethod(Method.ADD_NOTE);
            requestContainer.setToken(token);
            jsonRequest = gson.toJson(requestContainer);
            wsHandler.onRequestReceived (jsonRequest);

            AccessRight accessRight = new AccessRight();
            accessRight.setId(i+"");
            accessRight.setUserId(otherEmail);
            accessRight.setMode(1);
            accessRight.setNoteId(idNote);

            RequestContainer<AccessRight> requestContainer2 = new RequestContainer<AccessRight>();
            requestContainer2.setPayload(accessRight);
            requestContainer2.setRequestId(""+i);
            requestContainer2.setMethod(Method.ADD_ACCESS_RIGHT);
            requestContainer2.setToken(token);
            jsonRequest = gson.toJson(requestContainer2);
            wsHandler.onRequestReceived (jsonRequest);
        }

        //Получаем заметки для 2-го пользователю
        RequestContainer<String> reqCont = new RequestContainer<String>();
        reqCont.setPayload(otherEmail);
        reqCont.setRequestId("1");
        reqCont.setMethod(Method.GET_MY_NOTES);
        reqCont.setToken(otherToken);

        jsonRequest = gson.toJson(reqCont);

        wsHandler.onRequestReceived (jsonRequest);
        jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<List<Note>> regContainer =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<List<Note>>>(){}.getType());


        List<Note> getNotes = (List<Note>) regContainer.getPayload();
        //Проверяем что у 2-го пользователя появились заметки
        Assert.assertEquals(5,getNotes.size());



    }

    @Test
    public void testRemoveAccessRight() throws ProtocolException, RegistrationException, TokenValidatorException{
        //Даем 2-му пользователю права на заметки
        testAddAccessRight();

        ArrayList<Note> notes = new ArrayList<>();

        //От 1-го пользователя удаляем права 2-го на собственные заметки
        for(int i=0;i<5;i++){
            idNote =i+"";

            AccessRight accessRight = new AccessRight();
            accessRight.setId(i+"");
            accessRight.setUserId(otherEmail);
            accessRight.setNoteId(idNote);

            RequestContainer<AccessRight> requestContainer2 = new RequestContainer<AccessRight>();
            requestContainer2.setPayload(accessRight);
            requestContainer2.setRequestId(""+i);
            requestContainer2.setMethod(Method.REMOVE_ACCESS_RIGHT);
            requestContainer2.setToken(token);
            String jsonRequest = gson.toJson(requestContainer2);
            wsHandler.onRequestReceived (jsonRequest);
        }

        //Получаем заметки 2-го пользователя
        RequestContainer<String> reqCont = new RequestContainer<String>();
        reqCont.setPayload(otherEmail);
        reqCont.setRequestId("1");
        reqCont.setMethod(Method.GET_MY_NOTES);
        reqCont.setToken(otherToken);

        String jsonRequest = gson.toJson(reqCont);

        wsHandler.onRequestReceived (jsonRequest);
        String jsonResponse = wsHandler.onRequestReceived (jsonRequest);
        RequestContainer<List<Note>> regContainer =
                gson.fromJson(jsonResponse, new TypeToken<RequestContainer<List<Note>>>(){}.getType());


        List<Note> getNotes = (List<Note>) regContainer.getPayload();

        //Проверяем что у 2-го пользователя исчезли заметки
        Assert.assertEquals(0,getNotes.size());

    }




}
