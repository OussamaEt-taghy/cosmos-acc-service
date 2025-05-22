package ma.sofisoft.Services;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ma.sofisoft.Dtos.AccAcountDtos.CreateAccAccountRequest;
import ma.sofisoft.Dtos.AccAcountDtos.ResponseAccAccountDto;
import ma.sofisoft.Dtos.AccAcountDtos.UpdateAccAccountRequest;
import ma.sofisoft.Entities.AccAccount;
import ma.sofisoft.Exceptions.AccAccountExceptions.*;
import ma.sofisoft.Mappers.AccAccountMapper;
import ma.sofisoft.Repositories.AccAccountRepository;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@ApplicationScoped
public class AccAccountServiceImpl implements AccAccountService {
    private final AccAccountRepository accAccountRepository;
    private final AccAccountMapper accAccountMapper;
    @Inject
    public AccAccountServiceImpl(AccAccountRepository accAccountRepository, AccAccountMapper accAccountMapper) {
        this.accAccountRepository = accAccountRepository;
        this.accAccountMapper = accAccountMapper;
    }
    @Override
    @Transactional
    public ResponseAccAccountDto createAccount(CreateAccAccountRequest dto, String createdBy) {
        log.info("Creating new account with code: {}", dto.getCode());
        validateCreateAccountData(dto);
        if (accAccountRepository.existsByCode(dto.getCode())) {
            throw new DuplicateAccountCodeException(dto.getCode());
        }
        AccAccount parent = null;
        if (dto.getParentId() != null) {
            parent = accAccountRepository.findByIdOptional(dto.getParentId())
                    .orElseThrow(() -> new ParentAccountNotFoundException(dto.getParentId()));
        }
        AccAccount account = accAccountMapper.toEntityFromCreateDto(dto, parent, createdBy);
        accAccountRepository.persist(account);
        log.info("Account created with id: {}", account.getId());
        return accAccountMapper.toDTO(account);
    }
    @Override
    @Transactional
    public ResponseAccAccountDto updateAccount(Long id, UpdateAccAccountRequest dto, String updatedBy) {
        log.info("Updating account with id: {}", id);
        validateUpdateAccountData(dto);
        AccAccount account = accAccountRepository.findByIdOptional(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        if (dto.getCode() != null && !dto.getCode().equals(account.getCode())) {
            if (accAccountRepository.existsByCode(dto.getCode())) {
                throw new DuplicateAccountCodeException(dto.getCode());
            }
        }
        AccAccount parent = null;
        if (dto.getParentId() != null) {
            if (dto.getParentId().equals(id)) {
                throw new InvalidParentAccountException("Un compte ne peut pas être son propre parent");
            }
            if (isChildOf(dto.getParentId(), id)) {
                throw new InvalidParentAccountException("Impossible de définir un compte enfant comme parent (cela créerait un boucle)");
            }
            parent = accAccountRepository.findByIdOptional(dto.getParentId())
                    .orElseThrow(() -> new ParentAccountNotFoundException(dto.getParentId()));
        }
        accAccountMapper.toEntityFromUpdateDto(account, dto, parent, updatedBy);
        accAccountRepository.persist(account);
        log.info("Account updated successfully");
        return accAccountMapper.toDTO(account);
    }
    @Override
    @Transactional
    public AccAccount getAccountById(Long id) {
        log.debug("Fetching account with id: {}", id);
        return accAccountRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec ID: " + id));
    }
    @Override
    @Transactional
    public List<ResponseAccAccountDto> getAllAccounts() {
        log.debug("Fetching all accounts");
        return accAccountRepository.findAll().stream()
                .map(accAccountMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public String deleteAccount(Long id) {
        log.info("Suppression de compte avec identifiant: {}", id);
        AccAccount account = accAccountRepository.findByIdOptional(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        List<AccAccount> childAccounts = accAccountRepository.findChildAccounts(id);
        if (!childAccounts.isEmpty()) {
            throw new AccountHasChildrenException(id);
        }
        // TODO: Check if the account is used in transactions
        accAccountRepository.delete(account);
        log.info("Account deleted successfully");
        return "Compte supprimé avec succès";
    }

    // validation Methodes

    private void validateCreateAccountData(CreateAccAccountRequest dto) {
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new InvalidAccountDataException("Le code de compte est requis");
        }
        if (dto.getType() == null) {
            throw new InvalidAccountDataException("Le type de compte est requis");
        }

        if (dto.getIsAuxiliary() == null) {
            throw new InvalidAccountDataException(" IsAuxiliary est requis");
        }
    }

    private void validateUpdateAccountData(UpdateAccAccountRequest dto) {
        if (dto.getCode() != null && dto.getCode().trim().isEmpty()) {
            throw new InvalidAccountDataException("Le code de compte ne peut pas être vide");
        }
    }
    private boolean isChildOf(Long potentialChildId, Long parentId) {
        List<AccAccount> childAccounts = accAccountRepository.findChildAccounts(parentId);
        for (AccAccount child : childAccounts) {
            if (child.getId().equals(potentialChildId)) {
                return true;
            }
            if (isChildOf(potentialChildId, child.getId())) {
                return true;
            }
        }
        return false;
    }
}
