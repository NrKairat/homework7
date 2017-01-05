package ru.levelp.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.levelp.entities.BaseEntity;

/**
 * Created by кайрат on 19.12.2016.
 */
public abstract class BaseServiceMySQL<E extends BaseEntity,ID> implements BaseDAO<E,ID>  {
    private Session session;
    private Class<E> entityType;

    public BaseServiceMySQL(Class<E> entityType) {
        this.session = HibernateManager.getInstance().getSession();
        this.entityType = entityType;
    }

    public void add(E note){
//        System.out.println("beginTransaction();"+entityType.getName());
        session.beginTransaction();
//        System.out.println("saveOrUpdate");
        session.saveOrUpdate(note);
        session.getTransaction().commit();
    }

    public void delete(E note) {

    }

    public E get(ID id) {

        E note = (E) session.createCriteria(entityType)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        return note;
    }

    public void update(E note) {
        add(note);
    }

    public Session getSession() {
        return session;
    }
}
