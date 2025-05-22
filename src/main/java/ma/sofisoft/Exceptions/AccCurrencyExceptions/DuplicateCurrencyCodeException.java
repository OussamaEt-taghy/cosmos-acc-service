package ma.sofisoft.Exceptions.AccCurrencyExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class DuplicateCurrencyCodeException extends BusinessException {
    public DuplicateCurrencyCodeException(String code) {
        super("device avec code '" + code + "' existe déjà",
                "DUPLICATE_CURR_CODE",
                Response.Status.CONFLICT.getStatusCode());
    }
}
