package ma.sofisoft.Services;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ma.sofisoft.Dtos.AccCurrencyDtos.CreateAccCurrencyRequest;
import ma.sofisoft.Dtos.AccCurrencyDtos.ResponseAccCurrency;
import ma.sofisoft.Dtos.AccCurrencyDtos.UpdateAccCurrencyRequest;
import ma.sofisoft.Entities.AccCurrency;
import ma.sofisoft.Exceptions.AccCurrencyExceptions.CurrencyInUseException;
import ma.sofisoft.Exceptions.AccCurrencyExceptions.CurrencyNotFoundException;
import ma.sofisoft.Exceptions.AccCurrencyExceptions.DuplicateCurrencyCodeException;
import ma.sofisoft.Exceptions.AccCurrencyExceptions.InvalidCurrencyDataException;
import ma.sofisoft.Mappers.AccCurrencyMapper;
import ma.sofisoft.Repositories.AccCurrencyRepository;
import ma.sofisoft.Services.RemoteServiceChecker.CurrencyInUseChecker;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class AccCurrencyServiceImpl implements AccCurrencyService {
    private final AccCurrencyRepository accCurrencyRepository;
    private final AccCurrencyMapper accCurrencyMapper;
    private final CurrencyInUseChecker currencyInUseChecker;

    @Inject
    public AccCurrencyServiceImpl(AccCurrencyRepository accCurrencyRepository,
                                  AccCurrencyMapper accCurrencyMapper, CurrencyInUseChecker currencyInUseChecker) {
        this.accCurrencyRepository = accCurrencyRepository;
        this.accCurrencyMapper = accCurrencyMapper;
        this.currencyInUseChecker = currencyInUseChecker;
    }
    @Override
    @Transactional
    public ResponseAccCurrency createCurrency(CreateAccCurrencyRequest dto, String createdBy) {
        log.info("Creating new currency with code: {}", dto.getCode());
        validateCreateCurrencyData(dto);
        if (accCurrencyRepository.findByCode(dto.getCode()) != null) {
            throw new DuplicateCurrencyCodeException(dto.getCode());
        }
        AccCurrency accCurrency = accCurrencyMapper.toEntityFromCreateDto(dto, createdBy);
        accCurrencyRepository.persist(accCurrency);
        log.info("Currency created with ID: {}", accCurrency.getId());
        return accCurrencyMapper.toDto(accCurrency);
    }

    @Override
    @Transactional
    public ResponseAccCurrency updateCurrency(Long id, UpdateAccCurrencyRequest dto, String updatedBy) {
        log.info("Updating currency with ID: {}", id);
        validateUpdateCurrencyData(dto);
        if (!id.equals(dto.getId())) {
            throw new InvalidCurrencyDataException("L'ID du chemin et l'ID du DTO ne correspondent pas");
        }
        AccCurrency accCurrency = accCurrencyRepository.findByIdOptional(id)
                .orElseThrow(() -> new CurrencyNotFoundException(id));
        if (dto.getCode() != null && !dto.getCode().equals(accCurrency.getCode())) {
            AccCurrency existingWithCode = accCurrencyRepository.findByCode(dto.getCode());
            if (existingWithCode != null && !existingWithCode.getId().equals(id)) {
                throw new DuplicateCurrencyCodeException(dto.getCode());
            }
        }
        accCurrencyMapper.toEntityFromUpdateDto(accCurrency, dto, updatedBy);
        accCurrencyRepository.persist(accCurrency);
        log.info("Currency updated successfully");
        return accCurrencyMapper.toDto(accCurrency);
    }

    @Override
    @Transactional
    public ResponseAccCurrency getCurrencyById(Long id) {
        log.debug("Fetching currency with ID: {}", id);
        AccCurrency currency = accCurrencyRepository.findByIdOptional(id)
                .orElseThrow(() -> new CurrencyNotFoundException(id));
        return accCurrencyMapper.toDto(currency);
    }

    @Override
    @Transactional
    public List<ResponseAccCurrency> getCurrencies() {
        log.debug("Fetching all currencies");
        return accCurrencyRepository.findAll().stream()
                .map(accCurrencyMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public String deleteCurrency(Long id) {
        log.info("Deleting currency with ID: {}", id);
        AccCurrency currency = accCurrencyRepository.findByIdOptional(id)
                .orElseThrow(() -> new CurrencyNotFoundException(id));
        if (currencyInUseChecker.isInUse(id)) {
            throw new CurrencyInUseException(id);
        }
        accCurrencyRepository.delete(currency);
        log.info("Currency deleted successfully");
        return "Currency deleted successfully";
    }
    //  validation Methode
    private void validateCreateCurrencyData(CreateAccCurrencyRequest dto) {
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new InvalidCurrencyDataException("Le code de devise est requis");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidCurrencyDataException("Le nom de la devise est obligatoire");
        }
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new InvalidCurrencyDataException("Currency description is required");
        }
        validateRates(dto.getPurchaseRate(), "Purchase rate");
        validateRates(dto.getSalesRate(), "Sales rate");
        validateRates(dto.getReferenceRate(), "Reference rate");
    }
    private void validateUpdateCurrencyData(UpdateAccCurrencyRequest dto) {
        if (dto.getCode() != null && dto.getCode().trim().isEmpty()) {
            throw new InvalidCurrencyDataException("Le code de devise ne peut pas être vide");
        }
        if (dto.getName() != null && dto.getName().trim().isEmpty()) {
            throw new InvalidCurrencyDataException("Le nom de la devise ne peut pas être vide");
        }
        if (dto.getPurchaseRate() != null) {
            validateRates(dto.getPurchaseRate(), "Taux d'achat");
        }
        if (dto.getSalesRate() != null) {
            validateRates(dto.getSalesRate(), "Taux de vente");
        }
        if (dto.getReferenceRate() != null) {
            validateRates(dto.getReferenceRate(), "Taux de référence");
        }
    }
    private void validateRates(BigDecimal rate, String fieldName) {
        if (rate == null) {
            throw new InvalidCurrencyDataException(fieldName + " est requis");
        }
        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCurrencyDataException(fieldName + " doit être positif");
        }
    }
}
