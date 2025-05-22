package ma.sofisoft.Dtos.AccCurrencyDtos;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResponseAccCurrency {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal purchaseRate;
    private BigDecimal salesRate;
    private BigDecimal referenceRate;
    private Integer unit;
    private Integer subunit;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
