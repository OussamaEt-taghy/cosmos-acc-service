package ma.sofisoft.Entities;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "acc_currency", indexes = {
        @Index(name = "ix_acc_currency_code", columnList = "code", unique = true)
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Currencies used in the system : This entity is crucial for companies operating in an international or multi-currency context
public class AccCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code", nullable = false, length = 25)
    private String code;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Description", nullable = false, length = 50)
    private String description;

    @Column(name = "DescriptionCent", length = 50)
    private String descriptionCent;

    @Column(name = "Purchaserate", nullable = false, precision = 18, scale = 3)
    private BigDecimal purchaseRate;

    @Column(name = "Salesrate", nullable = false, precision = 18, scale = 3)
    private BigDecimal salesRate;

    @Column(name = "Referencerate", nullable = false, precision = 18, scale = 3)
    private BigDecimal referenceRate;

    @Column(name = "Unit")
    private Integer unit;

    @Column(name = "Subunit")
    private Integer subunit;

    @Builder.Default
    @Column(name = "Createdat", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "Createdby", nullable = false, length = 100)
    private String createdBy;

    @Builder.Default
    @Column(name = "Updatedat")
    private LocalDateTime updatedAt =  LocalDateTime.now();

    @Column(name = "Updatedby", length = 100)
    private String updatedBy;

}

