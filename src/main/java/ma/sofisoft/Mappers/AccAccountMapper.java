package ma.sofisoft.Mappers;

import ma.sofisoft.Dtos.AccAcountDtos.CreateAccAccountRequest;
import ma.sofisoft.Dtos.AccAcountDtos.ResponseAccAccountDto;
import ma.sofisoft.Dtos.AccAcountDtos.UpdateAccAccountRequest;
import ma.sofisoft.Entities.AccAccount;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi", imports = {BigDecimal.class, LocalDateTime.class})
public interface AccAccountMapper {
    // Entité ➜ ResponseDTO général For Reading
    @Mapping(target = "parentId", source = "parent.id")
    ResponseAccAccountDto toDTO(AccAccount entity);

    // Convert the CreateAccAccountRequest ➜ Entité For Creating
    @Mapping(target = "parent", source = "parent")
    @Mapping(target = "code", source = "dto.code")
    @Mapping(target = "type", source = "dto.type")
    @Mapping(target = "isAuxiliary", source = "dto.isAuxiliary")
    @Mapping(target = "openingBalance", expression = "java(dto.getOpeningBalance() != null ? dto.getOpeningBalance() : java.math.BigDecimal.ZERO)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    AccAccount toEntityFromCreateDto(CreateAccAccountRequest dto, AccAccount parent, String createdBy);


    // Convert the UpdateAccAccountRequest ➜ Entité For Update
    @Mapping(target = "parent", source = "parent")
    @Mapping(target = "code", source = "dto.code")
    @Mapping(target = "type", source = "dto.type")
    @Mapping(target = "isAuxiliary", source = "dto.isAuxiliary")
    @Mapping(target = "openingBalance", expression = "java(dto.getOpeningBalance() != null ? dto.getOpeningBalance() : java.math.BigDecimal.ZERO)")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void toEntityFromUpdateDto(@MappingTarget AccAccount entity, UpdateAccAccountRequest dto, AccAccount parent, String updatedBy);

    // To handle the case where parent is null
    @AfterMapping
    default void handleNullParent(@MappingTarget ResponseAccAccountDto dto, AccAccount entity) {
        if (entity.getParent() == null) {
            dto.setParentId(null);
        }
    }
}
