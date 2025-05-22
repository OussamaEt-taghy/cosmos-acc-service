package ma.sofisoft.Services;
import ma.sofisoft.Dtos.AccCurrencyDtos.CreateAccCurrencyRequest;
import ma.sofisoft.Dtos.AccCurrencyDtos.ResponseAccCurrency;
import ma.sofisoft.Dtos.AccCurrencyDtos.UpdateAccCurrencyRequest;
import java.util.List;
public interface AccCurrencyService {
    ResponseAccCurrency createCurrency(CreateAccCurrencyRequest dto, String createdBy);
    ResponseAccCurrency updateCurrency(Long id, UpdateAccCurrencyRequest dto, String updatedBy);
    ResponseAccCurrency getCurrencyById(Long id);
    List<ResponseAccCurrency> getCurrencies();
    String deleteCurrency(Long id);
}
