package ru.levelp.dao;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import ru.levelp.entities.BaseEntity;

public abstract class BaseServiceMongo<E extends BaseEntity,ID> implements BaseDAO<E,ID> {
    private Datastore db;
    private Class<E> entityType;

    public BaseServiceMongo(Class<E> entityType) {
        this.entityType = entityType;

        Morphia morphia = new Morphia();
        db = morphia.createDatastore(
                new MongoClient("localhost"), "levelupnotes");
        db.ensureIndexes();
//        System.out.println("create BaseServiceMongo <"+entityType+">")
    }

    public Datastore request() {
        return db;
    }

    public void add(E note) {


        db.save(note);
    }

    public void delete(E note) {
        db.delete(note);
    }

    public E get(ID id) {
//        System.out.println("E get(String id) "+entityType);
//        System.out.println(db.get(entityType, id).getClass());
        return db.get(entityType, id);
    }

    public void update(E note) {
        add(note);
    }


}
