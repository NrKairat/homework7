package ru.levelp.api;

import org.springframework.stereotype.Component;
import ru.levelp.api.entities.BaseRequest;
import ru.levelp.api.exceptions.ProtocolException;


/**
 * Created by кайрат on 31.12.2016.
 */
@Component("protocolValidator")
public class ProtocolValidator {

    private TokenValidator tokenValidator;


    public ProtocolValidator(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    public static final String[] METHODS = new String[]{
            Method.AUTHORIZE,
            Method.REGISTRATION,
            Method.GET_MY_NOTES,
            Method.ADD_NOTE,
            Method.GET_NOTE,
            Method.DELETE_NOTE,
            Method.EDIT_NOTE,
            Method.GET_USER,
            Method.GET_ALL_USERS,
            Method.ADD_ACCESS_RIGHT,
            Method.REMOVE_ACCESS_RIGHT
    };
    public void validate(BaseRequest baseRequest) throws ProtocolException {
        if(baseRequest.getRequestId()!=null&&
                baseRequest.getMethod()!=null){
            for(String m:METHODS){
                if(m.equals(baseRequest.getMethod())){
                    return;
                }
                //else{System.out.println(baseRequest.getMethod()+"!="+m);}
            }
        }
        //System.out.println("id="+baseRequest.getRequestId()+"method="+baseRequest.getMethod());
        throw new ProtocolException("protocol exception");
    }
}
