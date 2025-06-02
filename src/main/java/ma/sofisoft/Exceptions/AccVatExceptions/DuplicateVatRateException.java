package ma.sofisoft.Exceptions.AccVatExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

import java.math.BigDecimal;

public class DuplicateVatRateException extends BusinessException {
    public DuplicateVatRateException(BigDecimal rate) {
        super("TVA avec le taux  '" + rate + "' existe déjà",
                "DUPLICATE_VAT_RATE",
                Response.Status.CONFLICT.getStatusCode());
    }
}
