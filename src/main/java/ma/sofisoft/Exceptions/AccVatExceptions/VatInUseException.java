package ma.sofisoft.Exceptions.AccVatExceptions;

import jakarta.ws.rs.core.Response;
import ma.sofisoft.Exceptions.BusinessException;

public class VatInUseException extends BusinessException {
    public VatInUseException(Long id) {
        super("TVA avec ID: " + id + " est en cours d'utilisation et ne peut pas être supprimé",
                "VAT_IN_USE",
                Response.Status.CONFLICT.getStatusCode());
    }
}
