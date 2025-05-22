package ma.sofisoft.Repositories;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ma.sofisoft.Entities.AccVat;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccVatRepository implements PanacheRepository<AccVat> {
    public AccVat findByCode(String code) {
        return find("code", code).firstResult();
    }

    public Optional<AccVat> findByCodeOptional(String code) {
        return find("code", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public boolean existsByCodeAndIdNot(String code, Long id) {
        return count("code = ?1 and id != ?2", code, id) > 0;
    }

    public List<AccVat> findByPurchaseAccountId(Long accountId) {
        return list("purchaseAccount.id", accountId);
    }

    public List<AccVat> findBySalesAccountId(Long accountId) {
        return list("salesAccount.id", accountId);
    }

    public boolean existsByPurchaseAccountId(Long accountId) {
        return count("purchaseAccount.id", accountId) > 0;
    }

    public boolean existsBySalesAccountId(Long accountId) {
        return count("salesAccount.id", accountId) > 0;
    }
}
