package ma.sofisoft.Services;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ma.sofisoft.Dtos.AccVatDtos.CreateAccVatRequest;
import ma.sofisoft.Dtos.AccVatDtos.ResponseAccVat;
import ma.sofisoft.Dtos.AccVatDtos.UpdateAccVatRequest;
import ma.sofisoft.Entities.AccAccount;
import ma.sofisoft.Entities.AccVat;
import ma.sofisoft.Exceptions.AccAccountExceptions.AccountNotFoundException;
import ma.sofisoft.Exceptions.AccVatExceptions.*;
import ma.sofisoft.Mappers.AccVatMapper;
import ma.sofisoft.Repositories.AccVatRepository;
import ma.sofisoft.Services.RemoteServiceChecker.VatInUseChecker;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@ApplicationScoped
public class AccVatServiceImpl implements AccVatService {
    private final AccVatRepository accVatRepository;
    private final AccVatMapper accVatMapper;
    private final VatInUseChecker vatInUseChecker;
    private final AccAccountService accAccountService;
    @Inject
    public AccVatServiceImpl(
            AccVatRepository accVatRepository,
            AccVatMapper accVatMapper, VatInUseChecker vatInUseChecker, AccAccountService accAccountService
    ) {
        this.accVatRepository = accVatRepository;
        this.accVatMapper = accVatMapper;
        this.vatInUseChecker = vatInUseChecker;
        this.accAccountService = accAccountService;
    }

    @Override
    @Transactional
    public ResponseAccVat createVat(CreateAccVatRequest dto, String createdBy) {
        log.info("Creating new VAT with code: {}", dto.getCode());
        validateCreateVatData(dto);
        if (accVatRepository.findByCode(dto.getCode()) != null) {
            throw new DuplicateVatCodeException(dto.getCode());
        }
        // Récupération et validation des comptes
        AccAccount purchaseAccount = validateAndGetAccount(dto.getPurchaseAccountId(), "Purchase account");
        AccAccount salesAccount = validateAndGetAccount(dto.getSalesAccountId(), "Sales account");

        // Validation métier des comptes
        validateAccountsForVat(purchaseAccount, salesAccount);

        AccVat vat = accVatMapper.toEntityFromCreateDto(dto, purchaseAccount, salesAccount, createdBy);
        accVatRepository.persist(vat);
        log.info("VAT created with ID: {}", vat.getId());
        return accVatMapper.toDTO(vat);
    }

    @Override
    @Transactional
    public ResponseAccVat updateVat(Long id, UpdateAccVatRequest dto,String updatedBy) {
        log.info("Updating VAT with ID: {}", id);
        validateUpdateVatData(dto);
        if (dto.getId() != null && !id.equals(dto.getId())) {
            throw new InvalidVatDataException("L'ID du chemin et l'ID du DTO ne correspondent pas");
        }
        AccVat vat = accVatRepository.findByIdOptional(id)
                .orElseThrow(() -> new VatNotFoundException(id));
        if (dto.getCode() != null && !dto.getCode().equals(vat.getCode())) {
            AccVat existingWithCode = accVatRepository.findByCode(dto.getCode());
            if (existingWithCode != null && !existingWithCode.getId().equals(id)) {
                throw new DuplicateVatCodeException(dto.getCode());
            }
        }
        AccAccount purchaseAccount = null;
        AccAccount salesAccount = null;

        if (dto.getPurchaseAccountId() != null) {
            purchaseAccount = validateAndGetAccount(dto.getPurchaseAccountId(), "Purchase account");
        }

        if (dto.getSalesAccountId() != null) {
            salesAccount = validateAndGetAccount(dto.getSalesAccountId(), "Sales account");
        }
        if (purchaseAccount != null || salesAccount != null) {
            AccAccount finalPurchaseAccount = purchaseAccount != null ? purchaseAccount : vat.getPurchaseAccount();
            AccAccount finalSalesAccount = salesAccount != null ? salesAccount : vat.getSalesAccount();
            validateAccountsForVat(finalPurchaseAccount, finalSalesAccount);
        }
        accVatMapper.toEntityFromUpdateDto(vat, dto, purchaseAccount, salesAccount, updatedBy);
        accVatRepository.persist(vat);
        log.info("VAT updated successfully");
        return accVatMapper.toDTO(vat);
    }

    @Override
    @Transactional
    public ResponseAccVat getVatById(Long id) {
        log.debug("Fetching VAT with ID: {}", id);
        AccVat vat = accVatRepository.findByIdOptional(id)
                .orElseThrow(() -> new VatNotFoundException(id));
        return accVatMapper.toDTO(vat);
    }

    @Override
    @Transactional
    public List<ResponseAccVat> getVats() {
        log.debug("Fetching all VATs");
        return accVatRepository.findAll()
                .stream()
                .map(accVatMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String deleteVat(Long id) {
        log.info("Deleting VAT with ID: {}", id);
        AccVat vat = accVatRepository.findByIdOptional(id)
                .orElseThrow(() -> new VatNotFoundException(id));
        if (vatInUseChecker.isInUse(id)) {
            throw new VatInUseException(id);
        }
        accVatRepository.delete(vat);
        log.info("VAT deleted successfully");
        return "VAT deleted successfully";
    }

    // validation Methodes
    private void validateCreateVatData(CreateAccVatRequest dto) {
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new InvalidVatDataException("Le code de TVA est requis");
        }
        if (dto.getCode().length() > 25) {
            throw new InvalidVatDataException("Le code TVA ne peut pas dépasser 25 caractères");
        }
        validateVatRate(dto.getRate());
        if (dto.getPurchaseAccountId() == null) {
            throw new InvalidVatDataException("L'ID de compte d'achat est requis");
        }
        if (dto.getSalesAccountId() == null) {
            throw new InvalidVatDataException("L'ID de compte de vente est requis");
        }
    }

    private void validateUpdateVatData(UpdateAccVatRequest dto) {
        if (dto.getCode() != null) {
            if (dto.getCode().trim().isEmpty()) {
                throw new InvalidVatDataException("Le code TVA ne peut pas être vide");
            }
            if (dto.getCode().length() > 25) {
                throw new InvalidVatDataException("Le code TVA ne peut pas dépasser 25 caractères");
            }
        }
        if (dto.getRate() != null) {
            validateVatRate(dto.getRate());
        }
    }

    private void validateVatRate(BigDecimal rate) {
        if (rate == null) {
            throw new InvalidVatDataException("VAT rate is required");
        }
        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidVatDataException("VAT rate cannot be negative");
        }
        if (rate.compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidVatDataException("VAT rate cannot exceed 100%");
        }
    }

    private AccAccount validateAndGetAccount(Long accountId, String accountType) {
        try {
            return accAccountService.getAccountById(accountId);
        } catch (AccountNotFoundException e) {
            throw new InvalidVatAccountException(accountType + " not found with ID: " + accountId);
        } catch (Exception e) {
            throw new InvalidVatAccountException("Error retrieving " + accountType.toLowerCase() + ": " + e.getMessage());
        }
    }
    private void validateAccountsForVat(AccAccount purchaseAccount, AccAccount salesAccount) {
        if (purchaseAccount.getId().equals(salesAccount.getId())) {
            throw new InvalidVatAccountException("Le compte d'achat et le compte de vente ne peuvent pas être identiques");
        }
       // if (!isValidVatAccount(purchaseAccount)) {
        //    throw new InvalidVatAccountException("Type de compte d'achat non valide pour la TVA: " + purchaseAccount.getCode());
        //}

        // Validation du type de compte de vente
        //if (!isValidVatAccount(salesAccount)) {
        //    throw new InvalidVatAccountException("Type de compte de vente non valide pour la TVA: " + salesAccount.getCode());
        //}
    }

}
