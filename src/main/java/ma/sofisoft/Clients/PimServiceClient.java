package ma.sofisoft.Clients;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/qlq")
@RegisterRestClient(configKey = "cosmos-pim-service")
public interface PimServiceClient {
    @GET
    @Path("/currency-in-use/{currencyId}")
    boolean isCurrencyInUse(@PathParam("currencyId") Long currencyId);

    @GET
    @Path("/Vat-in-use/{vatId}")
    boolean isVatInUse(@PathParam("vatId") Long vatId);
}
