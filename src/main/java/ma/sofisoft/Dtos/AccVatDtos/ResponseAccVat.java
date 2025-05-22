package ma.sofisoft.Dtos.AccVatDtos;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAccVat {
    private Long id;
    private String code;
    private BigDecimal rate;
    private Long purchaseAccountId;
    private String purchaseAccountCode;
    private Long salesAccountId;
    private String salesAccountCode;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
