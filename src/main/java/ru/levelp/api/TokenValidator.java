package ru.levelp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.levelp.api.exceptions.TokenValidatorException;
import ru.levelp.dao.user.UserDAO;
import ru.levelp.dao.user.UserServiceMongo;

/**
 * Created by кайрат on 31.12.2016.
 */
@Component("tokenValidator")
public class TokenValidator {
    public static final String[] METHODS_TOKEN_NOT_REQUIRED = new String[]{
            Method.AUTHORIZE,
            Method.REGISTRATION
    };
    private UserDAO userServiceMongo;

    @Autowired
    public TokenValidator(UserDAO userServiceMongo) {
        this.userServiceMongo = userServiceMongo;
    }

    public void validate(String method, String token) throws TokenValidatorException {
        for(String m:METHODS_TOKEN_NOT_REQUIRED){
            if(method.equals(m)){
                return;
            }
        }
        if(token!=null){
            if(userServiceMongo.getByToken(token)!=null){
                return;
            }
        }
        throw new TokenValidatorException("");
    }
}
