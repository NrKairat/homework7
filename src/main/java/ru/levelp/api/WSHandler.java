package ru.levelp.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.levelp.api.entities.BaseRequest;
import ru.levelp.api.entities.ResponseContainer;
import ru.levelp.api.exceptions.ProtocolException;
import ru.levelp.api.exceptions.RegistrationException;
import ru.levelp.api.exceptions.TokenValidatorException;

/**
 * Created by кайрат on 30.12.2016.
 */
@Controller("wsHandler")
public class WSHandler {
    private Gson gson;
    private ProtocolValidator protocolValidator;
    private TokenValidator tokenValidator;
    private RequestExecutor requestExecutor;

    @Autowired
    public WSHandler(Gson gson, ProtocolValidator protocolValidator, TokenValidator tokenValidator, RequestExecutor requestExecutor) {
        this.gson = gson;
        this.protocolValidator = protocolValidator;
        this.tokenValidator = tokenValidator;
        this.requestExecutor = requestExecutor;
    }
    //Пришел запрос в этот метод по сокету от клиента
    public String/*void*/ onRequestReceived(String jsonRequest) throws ProtocolException, TokenValidatorException, RegistrationException {
        try{
            //Получаем базовый запрос из Json
            BaseRequest baseRequest = gson.fromJson(jsonRequest,BaseRequest.class);
            //Проверяем наличие Id и существующего метода
            protocolValidator.validate(baseRequest);
            //Проверяем наличие токена для методов требующих его
            tokenValidator.validate(baseRequest.getMethod(),baseRequest.getToken());
            ResponseContainer response= requestExecutor.execute(jsonRequest,baseRequest.getMethod());
            response.setCode(200);
            response.setRequestId(baseRequest.getRequestId());
            String answer = gson.toJson(response);
            //send answer to client
            return answer;
        }
        catch (JsonSyntaxException e){
            //e.printStackTrace();

            throw new JsonSyntaxException("");
        }
        catch (ProtocolException e) {
            throw new ProtocolException("");
        }
        catch (TokenValidatorException e) {

            throw new TokenValidatorException("");
        }

    }
}
