package ru.levelp.dao.user;

import org.hibernate.criterion.Restrictions;
import ru.levelp.dao.BaseServiceMySQL;
import ru.levelp.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by кайрат on 19.12.2016.
 */
public class UserServiceMySQL extends BaseServiceMySQL<User,String> implements UserDAO {

    public UserServiceMySQL(Class<User> entityType) {
        super(entityType);
    }

    @Override
    public List<User> getAll() {
        List<User> users = getSession().createCriteria(User.class).list();
        return users;
    }

    @Override
    public List<User> get(List<String> ids) {
        List<User> users = new ArrayList<User>();
        for(String id:ids){
            User user = (User) getSession().createCriteria(User.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            users.add(user);
        }
        return users;
    }

    @Override
    public User getByEmail(String email) {
        User user = (User) getSession().createCriteria(User.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
        return user;
    }

    @Override
    public User getByToken(String token) {
        return null;
    }

    @Override
    public void delete(String note) {

    }
}
