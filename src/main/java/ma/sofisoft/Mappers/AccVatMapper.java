package ma.sofisoft.Mappers;

import ma.sofisoft.Dtos.AccVatDtos.CreateAccVatRequest;
import ma.sofisoft.Dtos.AccVatDtos.ResponseAccVat;
import ma.sofisoft.Dtos.AccVatDtos.UpdateAccVatRequest;
import ma.sofisoft.Entities.AccAccount;
import ma.sofisoft.Entities.AccVat;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "cdi")
public interface AccVatMapper {
    // Entité ➜ ResponseDTo général For Reading
    @Mapping(target = "purchaseAccountId", source = "purchaseAccount.id")
    @Mapping(target = "purchaseAccountCode", source = "purchaseAccount.code")
    @Mapping(target = "salesAccountId", source = "salesAccount.id")
    @Mapping(target = "salesAccountCode", source = "salesAccount.code")
    ResponseAccVat toDTO(AccVat entity);

    // Convert the CreateAccVatRequest ➜ Entité For Creating
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "dto.code")
    @Mapping(target = "purchaseAccount", source = "purchaseAccountParam")
    @Mapping(target = "salesAccount", source = "salesAccountParam")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdByParam")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    AccVat toEntityFromCreateDto(CreateAccVatRequest dto, AccAccount purchaseAccountParam, AccAccount salesAccountParam, String createdByParam);

    // Convert the UpdateAccVatRequest ➜ Entité For Update
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "dto.code")
    @Mapping(target = "purchaseAccount", source = "purchaseAccountParam")
    @Mapping(target = "salesAccount", source = "salesAccountParam")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedBy", source = "updatedByParam")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void toEntityFromUpdateDto(@MappingTarget AccVat entity, UpdateAccVatRequest dto, AccAccount purchaseAccountParam, AccAccount salesAccountParam, String updatedByParam);

    // Handling cases where relationships can be null
    @AfterMapping
    default void handleNullRelations(@MappingTarget ResponseAccVat dto, AccVat entity) {
        if (entity.getPurchaseAccount() == null) {
            dto.setPurchaseAccountId(null);
            dto.setPurchaseAccountCode(null);
        }
        if (entity.getSalesAccount() == null) {
            dto.setSalesAccountId(null);
            dto.setSalesAccountCode(null);
        }
    }
}
