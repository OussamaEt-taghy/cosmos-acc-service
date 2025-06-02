package ma.sofisoft.Exceptions.AccAccountExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class AccAccountInUseException extends BusinessException {
    public AccAccountInUseException(Long accountId) {
        super("Le compte ID " + accountId + " ne peut pas être supprimé car il est utilisé",
                "ACCOUNT_IN_USE",
                Response.Status.CONFLICT.getStatusCode());
    }
}
