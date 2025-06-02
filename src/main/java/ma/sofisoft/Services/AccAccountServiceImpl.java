package ma.sofisoft.Services;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
// import ma.sofisoft.Clients.TranslationServiceClient;
import ma.sofisoft.Dtos.AccAcountDtos.CreateAccAccountRequest;
import ma.sofisoft.Dtos.AccAcountDtos.ResponseAccAccountDto;
import ma.sofisoft.Dtos.AccAcountDtos.UpdateAccAccountRequest;
// import ma.sofisoft.Dtos.TranslationDtos.CreateTranslationDataRequest;
// import ma.sofisoft.Dtos.TranslationDtos.ResponseTranslationDataDto;
import ma.sofisoft.Entities.AccAccount;
import ma.sofisoft.Exceptions.AccAccountExceptions.*;
import ma.sofisoft.Mappers.AccAccountMapper;
import ma.sofisoft.Repositories.AccAccountRepository;
// import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@ApplicationScoped
public class AccAccountServiceImpl implements AccAccountService {
    // private static final String TABLE_NAME = "acc_account";
    // private static final List<String> TRANSLATABLE_COLUMNS = Arrays.asList("code");

    // @Inject
    // @RestClient
    // private TranslationServiceClient translationClient;
    private final AccAccountRepository accAccountRepository;
    private final AccAccountMapper accAccountMapper;

    @Inject
    public AccAccountServiceImpl(AccAccountRepository accAccountRepository, AccAccountMapper accAccountMapper) {
        this.accAccountRepository = accAccountRepository;
        this.accAccountMapper = accAccountMapper;
    }

    // METHODE 1 : CREATE ACCOUNT
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
        // Créer les traductions si fournies
        // if (dto.getTranslations() != null && !dto.getTranslations().isEmpty()) {
        //     createTranslations(account.getId().intValue(), dto.getTranslations());
        // }
        log.info("Account created with id: {}", account.getId());
        ResponseAccAccountDto response = accAccountMapper.toDTO(account);
        // Charger les traductions dans la réponse
        // loadTranslations(response);
        return response;
    }

    // METHODE 2 : UPDATE ACCOUNT
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
    // METHODE 3 : GET ACCOUNT BY ID
    @Override
    @Transactional
    public AccAccount getAccountById(Long id) {
        log.debug("Fetching account with id: {}", id);
        return accAccountRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec ID: " + id));
    }

    // METHODE 4 : GET ALL ACCOUNTS
    @Override
    @Transactional
    public List<ResponseAccAccountDto> getAllAccounts() {
        log.debug("Fetching all accounts");
        return accAccountRepository.findAll().stream()
                .map(accAccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    // METHODE 5 : DELETE ACCOUNT
    @Override
    @Transactional
    public String deleteAccount(Long id) {
        log.info("Suppression de compte avec identifiant: {}", id);
        AccAccount account = accAccountRepository.findByIdOptional(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        // Vérifier les comptes enfants
        List<AccAccount> childAccounts = accAccountRepository.findChildAccounts(id);
        if (!childAccounts.isEmpty()) {
            throw new AccountHasChildrenException(id);
        }
        // Vérifier si le compte est utilisé dans d'autres tables
        if (!accAccountRepository.canBeDeleted(id)) {
            throw new AccAccountInUseException(id);
        }
        accAccountRepository.delete(account);
        log.info("Account '{}' deleted successfully", account.getCode());
        return "Compte supprimé avec succès";
    }

    // validation Methodes
    // METHODE 1 : VALIDATE CREATE ACCOUNT
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
    // METHODE 2 : VALIDATE UPDATE ACCOUNT
    private void validateUpdateAccountData(UpdateAccAccountRequest dto) {
        if (dto.getCode() != null && dto.getCode().trim().isEmpty()) {
            throw new InvalidAccountDataException("Le code de compte ne peut pas être vide");
        }
    }

    // METHODE 3 : IS CHILD OF ?
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

    // MÉTHODES PRIVÉES POUR GÉRER LES TRADUCTIONS - COMMENTÉES

    /*
    private void createTranslations(Integer accountId, Map<Long, Map<String, String>> translations) {
        // Construire la liste de toutes les traductions à créer
        List<CreateTranslationDataRequest> batchRequests = new ArrayList<>();
        translations.forEach((localeId, columnTranslations) -> {
            columnTranslations.forEach((columnName, value) -> {
                if (TRANSLATABLE_COLUMNS.contains(columnName) && value != null && !value.trim().isEmpty()) {
                    CreateTranslationDataRequest request = CreateTranslationDataRequest.builder()
                            .tableName(TABLE_NAME)
                            .keyId(accountId)
                            .columnName(columnName)
                            .localeId(localeId)
                            .translateValue(value)
                            .build();

                    batchRequests.add(request);
                }
            });
        });
        // Créer toutes les traductions en une seule fois (BATCH)
        if (!batchRequests.isEmpty()) {
            try {
                List<ResponseTranslationDataDto> results = translationClient.createBatchTranslations(batchRequests);
                log.info("Created {} translations in batch for account {}", results.size(), accountId);
            } catch (Exception e) {
                log.error("Failed to create batch translations for account {}: {}", accountId, e.getMessage());
            }
        }
    }

    private void loadTranslations(ResponseAccAccountDto response) {
        try {
            List<ResponseTranslationDataDto> translations =
                    translationClient.getTranslationsForRecord(TABLE_NAME, response.getId().intValue());
            if (!translations.isEmpty()) {
                Map<Long, Map<String, String>> translationMap = new HashMap<>();
                translations.forEach(translation -> {
                    translationMap.computeIfAbsent(translation.getLocaleId(), k -> new HashMap<>())
                            .put(translation.getColumnName(), translation.getTranslateValue());
                });
                response.setTranslations(translationMap);
            }
        } catch (Exception e) {
            log.error("Failed to load translations for account {}: {}", response.getId(), e.getMessage());
            // Ne pas bloquer si le chargement des traductions échoue
        }
    }
    */
}