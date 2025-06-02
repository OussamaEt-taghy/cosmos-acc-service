package ma.sofisoft.Exceptions.AccCurrencyExceptions;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;
public class InvalidCurrencyDataException extends BusinessException {
    public InvalidCurrencyDataException(String message) {
        super(message,
                "INVALID_CURR_DATA",
                Response.Status.BAD_REQUEST.getStatusCode());
    }
}
