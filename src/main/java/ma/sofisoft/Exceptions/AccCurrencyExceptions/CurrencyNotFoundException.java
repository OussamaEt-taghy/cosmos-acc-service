package ma.sofisoft.Exceptions.AccCurrencyExceptions;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;
public class CurrencyNotFoundException extends BusinessException {
    public CurrencyNotFoundException(Long id) {
        super("Devise non trouvée avec l'ID: " + id,
                "CURR_NOT_FOUND",
                Response.Status.NOT_FOUND.getStatusCode());
    }
    public CurrencyNotFoundException(String code) {
        super("Devise non trouvée avec le code : " + code,
                "CURR_NOT_FOUND",
                Response.Status.NOT_FOUND.getStatusCode());
    }
}