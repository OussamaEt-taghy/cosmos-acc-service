package ma.sofisoft.Dtos.AccCurrencyDtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateAccCurrencyRequest {
    @NotBlank(message = "Currency code is required")
    @Size(max = 25, message = "Currency code cannot exceed 25 characters")
    private String code;
    @NotBlank(message = "Currency name is required")
    @Size(max = 50, message = "Currency name cannot exceed 50 characters")
    private String name;
    private String description;
    private String descriptionCent;
    @NotNull(message = "Purchase rate is required")
    @Positive(message = "Purchase rate must be positive")
    private BigDecimal purchaseRate;
    @NotNull(message = "Sales rate is required")
    @Positive(message = "Sales rate must be positive")
    private BigDecimal salesRate;
    private BigDecimal referenceRate;
    private Integer unit;
    private Integer subunit;
}
