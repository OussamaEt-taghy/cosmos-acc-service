package ma.sofisoft.Dtos.AccAcountDtos;
import lombok.*;
import ma.sofisoft.ENUMs.AccountType;
import java.math.BigDecimal;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateAccAccountRequest {
    private Long id;
    private String code;
    private AccountType type;
    private Long parentId;
    private BigDecimal openingBalance;
    private Boolean isAuxiliary;
    //private Map<Long, Map<String, String>> translations;
}

