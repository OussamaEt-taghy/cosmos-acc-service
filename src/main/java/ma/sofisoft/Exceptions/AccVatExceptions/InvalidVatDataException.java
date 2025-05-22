package ma.sofisoft.Exceptions.AccVatExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class InvalidVatDataException extends BusinessException {
    public InvalidVatDataException(String message) {
        super(message,
                "INVALID_VAT_DATA",
                Response.Status.BAD_REQUEST.getStatusCode());
    }
}
