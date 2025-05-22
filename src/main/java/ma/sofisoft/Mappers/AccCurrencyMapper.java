package ma.sofisoft.Mappers;

import ma.sofisoft.Dtos.AccCurrencyDtos.CreateAccCurrencyRequest;
import ma.sofisoft.Dtos.AccCurrencyDtos.ResponseAccCurrency;
import ma.sofisoft.Dtos.AccCurrencyDtos.UpdateAccCurrencyRequest;
import ma.sofisoft.Entities.AccCurrency;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AccCurrencyMapper {
    // Entité ➜ RESPONSE DTO général Pour la lecture
    ResponseAccCurrency toDto(AccCurrency entity);

    // Convertir  CreateDto ➜ Entité
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdByParam")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    AccCurrency toEntityFromCreateDto(CreateAccCurrencyRequest dto,  String createdByParam);

    // Convertir le UpdateDto ➜ Entité Pour la mise à jour
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedBy", source = "updatedByParam")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void toEntityFromUpdateDto(@MappingTarget AccCurrency entity, UpdateAccCurrencyRequest dto,  String updatedByParam);
}
