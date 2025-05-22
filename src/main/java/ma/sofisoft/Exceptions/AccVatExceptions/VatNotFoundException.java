package ma.sofisoft.Exceptions.AccVatExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class VatNotFoundException extends BusinessException {
    public VatNotFoundException(Long id) {
        super("TVA non trouvée avec ID :" + id,
                "VAT_NOT_FOUND",
                Response.Status.NOT_FOUND.getStatusCode());
    }

    public VatNotFoundException(String code) {
        super("TVA non trouvée avec code: " + code,
                "VAT_NOT_FOUND",
                Response.Status.NOT_FOUND.getStatusCode());
    }
}
