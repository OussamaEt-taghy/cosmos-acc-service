package ma.sofisoft.Services;
import ma.sofisoft.Dtos.AccAcountDtos.CreateAccAccountRequest;
import ma.sofisoft.Dtos.AccAcountDtos.ResponseAccAccountDto;
import ma.sofisoft.Dtos.AccAcountDtos.UpdateAccAccountRequest;
import ma.sofisoft.Entities.AccAccount;
import java.util.List;

public interface AccAccountService {
    ResponseAccAccountDto createAccount(CreateAccAccountRequest dto, String createdBy);
    ResponseAccAccountDto updateAccount(Long id, UpdateAccAccountRequest dto, String updatedBy);
    AccAccount getAccountById(Long id);
    List<ResponseAccAccountDto> getAllAccounts();
    String deleteAccount(Long id);
}
