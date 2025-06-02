package ma.sofisoft.Dtos.AccAcountDtos;
import lombok.*;
import ma.sofisoft.ENUMs.AccountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResponseAccAccountDto {
    private Long id;
    private String code;
    private AccountType type;
    private Long parentId;
    private BigDecimal openingBalance;
    private Boolean isAuxiliary;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

   // private Map<Long, Map<String, String>> translations;
}

