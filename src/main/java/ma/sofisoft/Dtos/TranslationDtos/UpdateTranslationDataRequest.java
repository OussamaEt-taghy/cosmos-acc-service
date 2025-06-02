package ma.sofisoft.Dtos.TranslationDtos;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTranslationDataRequest {
    @Size(max = 50, message = "Le nom de la table ne peut pas dépasser 50 caractères")
    private String tableName;

    private Integer keyId;

    @Size(max = 255, message = "Le nom de la colonne ne peut pas dépasser 255 caractères")
    private String columnName;

    private Long localeId;

    @Size(max = 255, message = "La valeur traduite ne peut pas dépasser 255 caractères")
    private String translateValue;
}
