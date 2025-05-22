package ma.sofisoft.Exceptions.AccVatExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class InvalidVatAccountException extends BusinessException {
    public InvalidVatAccountException(String message) {
        super(message,
                "INVALID_VAT_ACCOUNT",
                Response.Status.BAD_REQUEST.getStatusCode());
    }
}
