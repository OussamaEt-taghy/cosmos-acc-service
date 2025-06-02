package ma.sofisoft.Entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "acc_vat", indexes = {
        @Index(name = "ix_acc_vat_code", columnList = "code", unique = true),
        @Index(name = "ix_acc_vat_rate", columnList = "rate", unique = true)
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
// VAT rates with their associated accounting accounts :
// entity manages the different VAT rates and their associated accounting accounts
public class AccVat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code", nullable = false, length = 25)
    private String code;

    @Column(name = "Rate", nullable = false, precision = 10, scale = 7)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Purchaseaccountid", nullable = false, foreignKey = @ForeignKey(name = "fk_acc_vat_purchaseaccount"))
    private AccAccount purchaseAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Salesaccountid", nullable = false, foreignKey = @ForeignKey(name = "fk_acc_vat_salesaccount"))
    private AccAccount salesAccount;

    @Builder.Default
    @Column(name = "Createdat", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "Createdby", nullable = false, length = 100)
    private String createdBy;

    @Builder.Default
    @Column(name = "updatedat")
    private LocalDateTime updatedAt=LocalDateTime.now();

    @Column(name = "Updatedby", length = 100)
    private String updatedBy;

}
