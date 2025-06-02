package ma.sofisoft.Dtos.TranslationDtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTranslationDataDto {
    private Long id;
    private String tableName;
    private Integer keyId;
    private String columnName;
    private Long localeId;
    private String localeName;
    private String translateValue;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private String updatedBy;
}
