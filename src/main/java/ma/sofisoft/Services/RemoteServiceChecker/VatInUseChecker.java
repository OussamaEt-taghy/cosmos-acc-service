package ma.sofisoft.Services.RemoteServiceChecker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ma.sofisoft.Clients.PimServiceClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class VatInUseChecker {
    @Inject
    @RestClient
    PimServiceClient pimServiceClient;
    public boolean isInUse(Long vatId) {
        return pimServiceClient.isVatInUse(vatId);
    }
}
