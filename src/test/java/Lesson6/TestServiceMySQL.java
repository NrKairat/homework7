package Lesson6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.levelp.dao.accessRight.AccessRightServiceMySQL;
import ru.levelp.dao.note.NoteServiceMySQL;
import ru.levelp.dao.user.UserServiceMySQL;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.Note;
import ru.levelp.entities.User;
import java.util.ArrayList;
import java.util.List;

/**
 Для реализации работы с заметками в Hibernate создано 3 таблицы: notes, users, note_rights
 В таблице note_rights хранятся объекты AccessRights с полями String id, userId, noteId, int mode
 В объектах класса Note отсуствует поле со списком прав List<AccessRights>
 Для получения списка прав пользователя используется таблица note_rights, и поиск поля "userId" в этой таблице
 Никаких связей между таблицами реализовано не было
* */
public class TestServiceMySQL {
    UserServiceMySQL  userServiceMySQL;
    NoteServiceMySQL noteServiceMySQL;
    List<User> userList;
    List<Note> noteList;
    List<String > names;
    @Before
    public void createBaseSQL(){
        userServiceMySQL = new UserServiceMySQL(User.class);
        userList = new ArrayList<User>();
        names = new ArrayList<String>();

        for(int i=0;i<6;i++){
            User user = new User();
            user.setEmail("box"+i+"@mail.ru");
            user.setName("Name"+i);
            user.setId(i+"");

            userServiceMySQL.add(user);
            userList.add(user);
            names.add(user.getId());

        }
//        System.out.println(userList.get(0).getEmail());

        noteServiceMySQL = new NoteServiceMySQL(Note.class);
        noteList =  new ArrayList<Note>();

        for(int i=0;i<6;i++){
            int j=i+6;
            Note note = new Note();
            note.setId(j+"");
            note.setAuthor(userList.get(i).getId());
            note.setBody("qwerty"+i);
            note.setTitle("title"+i);
            note.setCreated(System.currentTimeMillis());

            noteServiceMySQL.add(note);
            noteList.add(note);
        }

//        AccessRight(int mode, String userId, String noteId)
        AccessRight accessRight = new AccessRight(0,1+"",6+"");
        accessRight.setId(0+"");
        AccessRightServiceMySQL rightServiceMySQL = new AccessRightServiceMySQL(AccessRight.class);
        rightServiceMySQL.add(accessRight);



    }
    @Test
    public void testUserServiceMySQL(){

        //Проверка методов add() и get()
        System.out.println("userServiceMySQL"+userServiceMySQL);
        userServiceMySQL.get("0");
        User userT;
//        User userT = (User)userServiceMySQL.get("0");
//        System.out.println(userT==null);
//        Assert.assertEquals("0",userT.getId());


        //Проверка метода getAll()
        List<User> userTemp = userServiceMySQL.getAll();

        for(int i=0;i<userList.size();i++){
            Assert.assertEquals(userList.get(i).getId(),userTemp.get(i).getId());
            Assert.assertEquals(userList.get(i).getEmail(),userTemp.get(i).getEmail());
            Assert.assertEquals(userList.get(i).getName(),userTemp.get(i).getName());
        }

        //Проверка метода get(List<String> ids)
        userTemp = userServiceMySQL.get(names);

        for(int i=0;i<userList.size();i++){
            Assert.assertEquals(userList.get(i).getId(),userTemp.get(i).getId());
            Assert.assertEquals(userList.get(i).getEmail(),userTemp.get(i).getEmail());
            Assert.assertEquals(userList.get(i).getName(),userTemp.get(i).getName());
        }

        //Проверка метода getByEmail
        userT = userServiceMySQL.getByEmail(userList.get(0).getEmail());
        Assert.assertEquals(userList.get(0).getId(),userT.getId());
        Assert.assertEquals(userList.get(0).getEmail(),userT.getEmail());
        Assert.assertEquals(userList.get(0).getName(),userT.getName());
    }
    @Test
    public void testNoteServiceMySQL(){
        List<Note> notesTemp = new ArrayList<Note>();

        notesTemp = noteServiceMySQL.getForUser(userList.get(1).getId());
        Assert.assertEquals(2,notesTemp.size());
        Assert.assertEquals("7",notesTemp.get(0).getId());
        Assert.assertEquals("6",notesTemp.get(1).getId());

        noteServiceMySQL.delete("6");
        notesTemp = noteServiceMySQL.getForUser(userList.get(1).getId());
        Assert.assertEquals(1,notesTemp.size());
        Assert.assertEquals("7",notesTemp.get(0).getId());
    }
}
