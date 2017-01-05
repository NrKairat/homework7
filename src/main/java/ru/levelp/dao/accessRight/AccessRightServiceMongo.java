package ru.levelp.dao.accessRight;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;
import ru.levelp.dao.BaseServiceMongo;
import ru.levelp.entities.AccessRight;
import ru.levelp.entities.Note;

/**
 * Created by кайрат on 29.12.2016.
 */
@Service("accessRightServiceMongo")
public class AccessRightServiceMongo extends BaseServiceMongo<AccessRight,String> {
    public AccessRightServiceMongo() {
        super(AccessRight.class);
    }

    @Override
    public void delete(String note) {

    }

    public void addAccessRight(AccessRight accessRight){
        request().save(accessRight);
    }

    public void drop() {
        Query<AccessRight> query = request().createQuery(AccessRight.class);
        request().delete(query);
    }

    public void removeAccessRight(AccessRight accessRight) {
        Query<AccessRight> query = request().createQuery(AccessRight.class);
        query.or(
                query.criteria("noteId").equal(accessRight.getNoteId()),
                query.criteria("userId").equal(accessRight.getUserId())
        );
        request().delete(query);
    }
}
