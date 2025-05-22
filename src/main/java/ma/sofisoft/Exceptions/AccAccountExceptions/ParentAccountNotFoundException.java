package ma.sofisoft.Exceptions.AccAccountExceptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class ParentAccountNotFoundException extends BusinessException {
    public ParentAccountNotFoundException(Long id) {
        super("Compte parent introuvable avec l'ID : " + id,
                "PARENT_ACC_NOT_FOUND",
                Response.Status.NOT_FOUND.getStatusCode());
    }
}
