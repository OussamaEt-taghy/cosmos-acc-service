package ma.sofisoft.Controllers;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Dtos.AccVatDtos.CreateAccVatRequest;
import ma.sofisoft.Dtos.AccVatDtos.UpdateAccVatRequest;
import ma.sofisoft.Exceptions.ErrorResponse;
import ma.sofisoft.Services.AccAccountService;
import ma.sofisoft.Services.AccVatService;
import ma.sofisoft.Services.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
@Path("/ACC-API/VAT")
@Tag(name = "VAT API", description = "TVA MANAGEMENT")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AccVatResource {
    @Inject
    AccVatService accVatService;
    @Inject
    AccAccountService accAccountService;
    @Inject
    SecurityUtils securityUtils;
    @POST
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Create a VAT", description = "Create a new VAT rule")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "TVA créée avec succès"),
            @APIResponse(responseCode = "400", description = "Données de TVA invalides ou configuration de compte incorrecte",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "Compte référencé introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "Le code TVA existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response createVat(@Valid CreateAccVatRequest dto) {
        String createdBy = securityUtils.getCurrentUserName();
        return Response.ok(accVatService.createVat(dto, createdBy)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Update a VAT", description = "Updates the information of a VAT rule")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "TVA mise à jour avec succès"),
            @APIResponse(responseCode = "400", description = "Données de TVA invalides ou configuration de compte incorrecte",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "TVA ou compte référencé introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "Le code TVA existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response updateVat(@PathParam("id") Long id, @Valid UpdateAccVatRequest dto) {
        String updatedBy = securityUtils.getCurrentUserName();
        return Response.ok(accVatService.updateVat(id, dto, updatedBy)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Get VAT by ID", description = "Returns a VAT rule based on its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "TVA récupérée avec succès"),
            @APIResponse(responseCode = "404", description = "TVA non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response getVatById(@PathParam("id") Long id) {
        return Response.ok(accVatService.getVatById(id)).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "VAT List", description = "Returns all VAT rules")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Liste des TVA récupérée avec succès"),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response getVats() {
        return Response.ok(accVatService.getVats()).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Remove a VAT", description = "Delete a VAT rule")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "TVA supprimée avec succès"),
            @APIResponse(responseCode = "404", description = "TVA non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "La TVA est utilisée et ne peut pas être supprimée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response deleteVat(@PathParam("id") Long id) {
        return Response.ok(accVatService.deleteVat(id)).build();
    }
}
