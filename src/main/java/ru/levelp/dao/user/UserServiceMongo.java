package ru.levelp.dao.user;

import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;
import ru.levelp.dao.BaseServiceMongo;
import ru.levelp.entities.User;

import java.util.ArrayList;
import java.util.List;
@Service("userServiceMongo")
public class UserServiceMongo extends BaseServiceMongo<User,String> implements UserDAO {

    public UserServiceMongo() {
        super(User.class);
    }
    @Override
    public List<User> getAll() {
        List users = request().createQuery(User.class)
                .order("name")
                .asList();
        return users;
    }
    @Override
    public List<User> get(List<String> ids) {

        return request().createQuery(User.class)
                .field("id").in(ids)
                .order("name")
                .asList();
    }
    @Override
    public User getByEmail(String email) {
        return request().createQuery(User.class)
                .field("email").equal(email)
                .get();
    }

    @Override
    public User getByToken(String token) {
        return request().createQuery(User.class)
                .field("token").equal(token)
                .get();
    }


    public void drop(){
        Query<User> query = request().createQuery(User.class);
        request().delete(query);
    }

    @Override
    public void delete(String note) {

    }


}
