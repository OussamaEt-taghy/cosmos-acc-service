package ma.sofisoft.Clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/PIM-API")
@RegisterRestClient(configKey = "cosmos-pim-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PimServiceClient {
    @GET
    @Path("/currency-in-use/{currencyId}")
    boolean isCurrencyInUse(@PathParam("currencyId") Long currencyId);

    @GET
    @Path("/Vat-in-use/{vatId}")
    boolean isVatInUse(@PathParam("vatId") Long vatId);
}
