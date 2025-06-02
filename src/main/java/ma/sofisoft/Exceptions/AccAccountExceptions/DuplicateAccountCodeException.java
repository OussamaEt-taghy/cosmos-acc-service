package ma.sofisoft.Exceptions.AccAccountExceptions;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;
public class DuplicateAccountCodeException extends BusinessException {
    public DuplicateAccountCodeException(String code) {
        super("Compte avec code'" + code + "' existe déjà",
                "DUPLICATE_ACC_CODE",
                Response.Status.CONFLICT.getStatusCode());
    }
}
