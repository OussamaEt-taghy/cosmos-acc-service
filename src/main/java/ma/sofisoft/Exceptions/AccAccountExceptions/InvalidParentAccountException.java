package ma.sofisoft.Exceptions.AccAccountExceptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class InvalidParentAccountException extends BusinessException {
    public InvalidParentAccountException(String message) {
        super(message,
                "INVALID_PARENT_ACC",
                Response.Status.BAD_REQUEST.getStatusCode());
    }
}
