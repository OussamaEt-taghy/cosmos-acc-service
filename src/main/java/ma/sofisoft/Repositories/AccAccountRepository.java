package ma.sofisoft.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ma.sofisoft.Entities.AccAccount;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccAccountRepository implements PanacheRepository<AccAccount> {
    public List<AccAccount> findByParentId(Long parentId) {
        if (parentId == null) {
            return list("parent is null");
        }
        return list("parent.id = ?1", parentId);
    }
    public List<AccAccount> findChildAccounts(Long parentId) {
        return list("parent.id", parentId);
    }
    public Optional<AccAccount> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }
    public boolean existsByCode(String code) {
        return count("code", code) > 0;
    }
    // Méthode pour vérifier si un autre compte (différent par l’ID) a déjà ce code
    // Utile pour éviter les doublons lors d’une mise à jour d’un compte
    public boolean existsByCodeAndIdNot(String code, Long id) {
        return count("code = ?1 and id != ?2", code, id) > 0;
    }

    public boolean canBeDeleted(Long accountId) {
        // Vérifier les comptes enfants
        if (count("parent.id", accountId) > 0) {
            return false;
        }

        // Vérifier les références dans acc_vat
        long vatReferences = getEntityManager()
                .createQuery("SELECT COUNT(v) FROM AccVat v WHERE v.purchaseAccount.id = :id OR v.salesAccount.id = :id", Long.class)
                .setParameter("id", accountId)
                .getSingleResult();

        return vatReferences == 0;
    }
}
