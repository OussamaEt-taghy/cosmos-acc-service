package ma.sofisoft.Dtos.AccAcountDtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.sofisoft.ENUMs.AccountType;
import java.math.BigDecimal;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateAccAccountRequest {
    @NotBlank(message = "Account code is required")
    private String code;
    @NotNull(message = "Account type is required")
    private AccountType type;
    private Long parentId;
    private BigDecimal openingBalance;
    @NotNull(message = "IsAuxiliary flag is required")
    private Boolean isAuxiliary;
}

