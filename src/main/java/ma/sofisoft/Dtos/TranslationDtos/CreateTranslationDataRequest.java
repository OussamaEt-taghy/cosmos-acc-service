package ma.sofisoft.Dtos.TranslationDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTranslationDataRequest {

    @NotBlank(message = "Le nom de la table est requis")
    @Size(max = 50, message = "Le nom de la table ne peut pas dépasser 50 caractères")
    private String tableName;

    @NotNull(message = "L'ID de la clé est requis")
    private Integer keyId;

    @NotBlank(message = "Le nom de la colonne est requis")
    @Size(max = 255, message = "Le nom de la colonne ne peut pas dépasser 255 caractères")
    private String columnName;

    @NotBlank(message = "Le code de la locale est requis")
    private Long localeId; // Utiliser le code au lieu de l'ID pour plus de simplicité

    @NotBlank(message = "La valeur traduite est requise")
    @Size(max = 255, message = "La valeur traduite ne peut pas dépasser 255 caractères")
    private String translateValue;
}
