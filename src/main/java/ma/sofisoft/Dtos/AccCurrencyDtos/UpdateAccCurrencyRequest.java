package ma.sofisoft.Dtos.AccCurrencyDtos;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateAccCurrencyRequest {
    private Long id;
    @Size(max = 25, message = "Currency code cannot exceed 25 characters")
    private String code;
    @Size(max = 50, message = "Currency name cannot exceed 50 characters")
    private String name;
    private String description;
    private String descriptionCent;
    @Positive(message = "Purchase rate must be positive")
    private BigDecimal purchaseRate;
    @Positive(message = "Sales rate must be positive")
    private BigDecimal salesRate;
    @Positive(message = "Reference rate must be positive")
    private BigDecimal referenceRate;
    private Integer unit;
    private Integer subunit;
}
