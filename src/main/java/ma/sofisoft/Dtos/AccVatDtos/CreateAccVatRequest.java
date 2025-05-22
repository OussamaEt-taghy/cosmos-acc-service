package ma.sofisoft.Dtos.AccVatDtos;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccVatRequest {
    @NotBlank(message = "VAT code is required")
    @Size(max = 25, message = "VAT code cannot exceed 25 characters")
    private String code;
    @NotNull(message = "VAT rate is required")
    @DecimalMin(value = "0.0", message = "VAT rate cannot be negative")
    @DecimalMax(value = "100.0", message = "VAT rate cannot exceed 100%")
    private BigDecimal rate;
    @NotNull(message = "Purchase account ID is required")
    @Positive(message = "Purchase account ID must be positive")
    private Long purchaseAccountId;
    @NotNull(message = "Sales account ID is required")
    @Positive(message = "Sales account ID must be positive")
    private Long salesAccountId;
}
