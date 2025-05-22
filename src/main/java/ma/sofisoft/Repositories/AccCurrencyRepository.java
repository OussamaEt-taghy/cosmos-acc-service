package ma.sofisoft.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ma.sofisoft.Entities.AccCurrency;

import java.util.Optional;

@ApplicationScoped
public class AccCurrencyRepository implements PanacheRepository<AccCurrency> {

    public AccCurrency findByCode(String code) {
        return find("code", code).firstResult();
    }

    public Optional<AccCurrency> findByCodeOptional(String code) {
        return find("code", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code", code) > 0;
    }

    public boolean existsByCodeAndIdNot(String code, Long id) {
        return count("code = ?1 and id != ?2", code, id) > 0;
    }
}
