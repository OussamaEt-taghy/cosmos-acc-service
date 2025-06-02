package ma.sofisoft.Exceptions.AccCurrencyExceptions;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class CurrencyInUseException extends BusinessException {
    public CurrencyInUseException(Long id) {
        super("Devise avec ID : " + id + " est en cours d'utilisation et ne peut pas être supprimé",
                "CURR_IN_USE",
                Response.Status.CONFLICT.getStatusCode());
    }
}
