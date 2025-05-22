package ma.sofisoft.Dtos.AccVatDtos;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccVatRequest {
    private Long id;
    private String code;
    @DecimalMin(value = "0.0", message = "VAT rate cannot be negative")
    @DecimalMax(value = "100.0", message = "VAT rate cannot exceed 100%")
    private BigDecimal rate;
    @Positive(message = "Purchase account ID must be positive")
    private Long purchaseAccountId;
    @Positive(message = "Sales account ID must be positive")
    private Long salesAccountId;
}
