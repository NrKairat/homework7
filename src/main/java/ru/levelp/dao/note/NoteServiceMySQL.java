package ru.levelp.dao.note;

import org.hibernate.criterion.Restrictions;
import ru.levelp.dao.BaseServiceMySQL;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.Note;


import java.util.List;

/**
 * Created by кайрат on 19.12.2016.
 */
public class NoteServiceMySQL extends BaseServiceMySQL<Note,String> implements NoteDAO {

    public NoteServiceMySQL(Class<Note> entityType) {
        super(entityType);
    }

    @Override
    public List<Note> getForUser(String id) {
        List<Note> noteList = (List<Note>) getSession().createCriteria(Note.class)
                .add(Restrictions.eq("author", id))
                .list();
        List<AccessRight> accessList = (List<AccessRight>) getSession().createCriteria(AccessRight.class)
                .add(Restrictions.eq("userId", id))
                .list();
//        System.out.println("NsMySQL"+noteList.size()+"/"+accessList.size());
        for(int i=0;i<accessList.size();i++) {

            String noteId = accessList.get(i).getNoteId();
            Note note = get(noteId);
            noteList.add(note);
        }

        return noteList;

    }

    @Override
    public void delete(String note) {

    }
}
