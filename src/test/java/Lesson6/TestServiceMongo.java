package Lesson6;

import org.junit.Assert;
import org.junit.*;
import ru.levelp.dao.accessRight.AccessRightServiceMongo;
import ru.levelp.dao.note.NoteServiceMongo;
import ru.levelp.dao.user.UserServiceMongo;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.Note;
import ru.levelp.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 1. Unit-тесты для всех методов сервисов БД.
 2. Реализации сервисов БД для MySQL Hibernate.
 3. Тестирование Hibernate сервисов.

    Для реализации работы с заметками в Монго создано 3 коллекции: notes, users, access_rights
    В коллекции access_rights хранятся объекты AccessRights с полями String id, userId, noteId, int mode
    В объектах класса Note отсуствует поле со списком прав List<AccessRights>
 */
public class TestServiceMongo {

    UserServiceMongo userServiceMongo;
    NoteServiceMongo noteServiceMongo;
    List<User> userList;
    List<String> names;
    List<Note> noteList;

    @Before
    // Создаем базу Монго
    public void createMongoBase(){

        userServiceMongo = new UserServiceMongo();
        userList = new ArrayList<User>();
        names = new ArrayList<String>();

        //В цикле создаем 6 пользователей. Добавляем их в коллекцию и сохраняем в БД в коллекцию "users"
        for(int i=0;i<6;i++){
            User user = new User();
            user.setEmail("box"+i+"@mail.ru");
            user.setName("Name"+i);
            user.setId(i+"");
            user.setPwdHash("12345");

            userList.add(user);
            userServiceMongo.add(user);
            names.add(user.getId());
        }



        noteList =  new ArrayList<Note>();
        noteServiceMongo = new NoteServiceMongo();
        //В цикле создаем 6 заметок. Добавляем их в коллекцию и сохраняем в БД в коллекцию "notes"
        for(int i=0;i<6;i++){
            int j=i+6;
            Note note = new Note();
            note.setId(j+"");
            note.setAuthor(userList.get(i).getId());
            note.setBody("qwerty");
            note.setTitle("title");
            note.setCreated(System.currentTimeMillis());

            noteList.add(note);
            noteServiceMongo.add(note);
        }
        // Создаем объект AccessRight, заносим его в БД в коллекцию "AccessRight"
        // Даем второму пользователю(id = 1) право редактировать заметку первого пользователя (noteId=6)
        //        AccessRight(int mode, String userId, String noteId)
        AccessRight accessRight = new AccessRight(0,1+"",6+"");
        accessRight.setId(0+"");
        AccessRightServiceMongo rightServiceMySQL = new AccessRightServiceMongo();
        rightServiceMySQL.add(accessRight);


    }

    @Test
    //Проверка методов работы с пользователями в Монго (UserServiceMongo)
    public void testUserServiceMongo(){
        //Проверка методов add() и get()
        //Получаем первого пользователя и проверяем его id
        User userT = (User)userServiceMongo.get("0");
        Assert.assertEquals("0",userT.getId());


        //Проверка метода getAll()
        //Получаем список всех пользователей и сохраняем его в userTemp
        List<User> userTemp = userServiceMongo.getAll();
        //Перебираем полученную коллекцию и проверяем поля id, email, name
        for(int i=0;i<userList.size();i++){
            Assert.assertEquals(userList.get(i).getId(),userTemp.get(i).getId());
            Assert.assertEquals(userList.get(i).getEmail(),userTemp.get(i).getEmail());
            Assert.assertEquals(userList.get(i).getName(),userTemp.get(i).getName());
        }

        //Проверка метода get(List<String> ids)
        userTemp = userServiceMongo.get(names);

        for(int i=0;i<userList.size();i++){
            Assert.assertEquals(userList.get(i).getId(),userTemp.get(i).getId());
            Assert.assertEquals(userList.get(i).getEmail(),userTemp.get(i).getEmail());
            Assert.assertEquals(userList.get(i).getName(),userTemp.get(i).getName());
        }

        //Проверка метода getByEmail
        //Вызываем метод getByEmail, передаем ему email первого пользователя, сохраняем полученного пользователя
        //в userT и проверяем его поля
        userT = userServiceMongo.getByEmail(userList.get(0).getEmail());
        Assert.assertEquals(userList.get(0).getId(),userT.getId());
        Assert.assertEquals(userList.get(0).getEmail(),userT.getEmail());
        Assert.assertEquals(userList.get(0).getName(),userT.getName());


    }

    @Test
    //Проверка методов работы с заметками в Монго (NoteServiceMongo)
    public void testNoteServiceMongo(){
        List<Note> notesTemp = new ArrayList<Note>();

        //Проверяем метод getForUser, который возвращает заметки, созданные пользователем и те заметки, на которые у него
        //есть права
        //Для примера вызываем метод для второго пользователя(id=1)
        notesTemp = noteServiceMongo.getForUser(userList.get(1).getId());
        //Проверяем что в коллекции 2 элемента
        Assert.assertEquals(2,notesTemp.size());
        //Проверяем id первой заметки, созданной пользователем
        Assert.assertEquals("7",notesTemp.get(0).getId());
        //Проверяем id второй заметки, права на которую есть у второго пользователя
        Assert.assertEquals("6",notesTemp.get(1).getId());



    }
}
