package ma.sofisoft.Exceptions.AccAccountExceptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;


public class InvalidAccountDataException extends BusinessException {
    public InvalidAccountDataException(String message) {
        super(message,
                "INVALID_ACC_DATA",
                Response.Status.BAD_REQUEST.getStatusCode());
    }
}
