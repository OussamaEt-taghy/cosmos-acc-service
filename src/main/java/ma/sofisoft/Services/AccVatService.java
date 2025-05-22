package ma.sofisoft.Services;

import ma.sofisoft.Dtos.AccVatDtos.CreateAccVatRequest;
import ma.sofisoft.Dtos.AccVatDtos.ResponseAccVat;
import ma.sofisoft.Dtos.AccVatDtos.UpdateAccVatRequest;
import ma.sofisoft.Entities.AccAccount;

import java.util.List;

public interface AccVatService {
    ResponseAccVat createVat(CreateAccVatRequest dto, String createdBy);
    ResponseAccVat updateVat(Long id, UpdateAccVatRequest dto,String updatedBy);
    ResponseAccVat getVatById(Long id);
    List<ResponseAccVat> getVats();
    String deleteVat(Long id);

}
