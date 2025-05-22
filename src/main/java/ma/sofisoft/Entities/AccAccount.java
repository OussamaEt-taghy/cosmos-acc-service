package ma.sofisoft.Entities;
import jakarta.persistence.*;
import lombok.*;
import ma.sofisoft.ENUMs.AccountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "acc_account", indexes = {
        @Index(name = "ix_acc_account_code", columnList = "code", unique = true)
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Accounting accounts (chart of accounts) : This entity allows the company's accounting plan to be structured
public class AccAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Code", nullable = false, length = 25)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "TypeAccount", nullable = false)
    private AccountType type;

    @ManyToOne
    @JoinColumn(name = "Parentid", foreignKey = @ForeignKey(name = "fk_acc_account_parent"))
    private AccAccount parent;

    @Builder.Default
    @Column(name = "Openingbalance", precision = 15, scale = 2)
    private BigDecimal openingBalance = BigDecimal.ZERO;

    @Column(name = "Isauxiliary", nullable = false)
    private Boolean isAuxiliary;

    @Builder.Default
    @Column(name = "Createdat", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "Createdby", nullable = false, length = 100)
    private String createdBy ;

    @Builder.Default
    @Column(name = "Updatedat")
    private LocalDateTime updatedAt= LocalDateTime.now();

    @Column(name = "Updatedby", length = 100)
    private String updatedBy;
}
