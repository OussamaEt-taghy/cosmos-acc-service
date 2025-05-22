package ma.sofisoft.Exceptions.AccAccountExceptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;
public class AccountHasChildrenException extends BusinessException {
    public AccountHasChildrenException(Long id) {
        super("Impossible de supprimer le compte avec l'ID :" + id + " parce qu'il a des comptes enfants",
                "ACC_HAS_CHILDREN",
                Response.Status.CONFLICT.getStatusCode());
    }
}
