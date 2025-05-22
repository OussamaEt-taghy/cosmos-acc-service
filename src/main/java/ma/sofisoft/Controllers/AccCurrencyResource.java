package ma.sofisoft.Controllers;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Dtos.AccCurrencyDtos.CreateAccCurrencyRequest;
import ma.sofisoft.Dtos.AccCurrencyDtos.UpdateAccCurrencyRequest;
import ma.sofisoft.Exceptions.ErrorResponse;
import ma.sofisoft.Services.AccCurrencyService;
import ma.sofisoft.Services.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
@Path("/ACC-API/CURRENCY")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "CURRENCY API", description = "Currency Management ")
@ApplicationScoped
public class AccCurrencyResource {
    @Inject
    AccCurrencyService accCurrencyService;
    @Inject
    SecurityUtils securityUtils;
    @POST
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Create a currency", description = "Create a new currency")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Currency créée avec succès"),
            @APIResponse(responseCode = "400", description = "Données de devise non valides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "Le code de devise existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response createCurrency(CreateAccCurrencyRequest dto) {
        String createdBy = securityUtils.getCurrentUserName();
        return Response.ok(accCurrencyService.createCurrency(dto, createdBy)).build();
    }
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Update a currency", description = "Updates an existing currency")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Currency mis à jour avec succès"),
            @APIResponse(responseCode = "400", description = "Données de devise non valides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "Devise non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "Le code de devise existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response updateCurrency(@PathParam("id") Long id, UpdateAccCurrencyRequest dto) {
        String updatedBy = securityUtils.getCurrentUserName();
        return Response.ok(accCurrencyService.updateCurrency(id, dto, updatedBy)).build();
    }
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Get currency by ID", description = "Returns a currency based on its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Devise récupérée avec succès"),
            @APIResponse(responseCode = "404", description = "Devise non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response getCurrencyById(@PathParam("id") Long id) {
        return Response.ok(accCurrencyService.getCurrencyById(id)).build();
    }
    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "List of currencies", description = "Returns the list of all currencies")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Devises récupérées avec succès"),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response getCurrencies() {
        return Response.ok(accCurrencyService.getCurrencies()).build();
    }
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Delete a currency", description = "Deletes a currency based on its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Devise supprimée avec succès"),
            @APIResponse(responseCode = "404", description = "Devise non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "La devise est en cours d'utilisation et ne peut pas être supprimée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveu",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response deleteCurrency(@PathParam("id") Long id) {
        return Response.ok(accCurrencyService.deleteCurrency(id)).build();
    }
}
