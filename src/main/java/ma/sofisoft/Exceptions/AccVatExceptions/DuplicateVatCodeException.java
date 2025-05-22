package ma.sofisoft.Exceptions.AccVatExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class DuplicateVatCodeException extends BusinessException {
    public DuplicateVatCodeException(String code) {
        super("VAT with code '" + code + "' existe déjà",
                "DUPLICATE_VAT_CODE",
                Response.Status.CONFLICT.getStatusCode());
    }
}
