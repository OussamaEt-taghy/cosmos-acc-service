package ma.sofisoft.Exceptions.AccAccountExceptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(Long id) {
        super("Compte non trouvé avec l'identifiant: " + id,
                "ACC_NOT_FOUND",
                Response.Status.NOT_FOUND.getStatusCode());
    }
}
