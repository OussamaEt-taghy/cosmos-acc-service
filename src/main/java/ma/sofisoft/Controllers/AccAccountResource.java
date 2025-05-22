package ma.sofisoft.Controllers;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.sofisoft.Dtos.AccAcountDtos.CreateAccAccountRequest;
import ma.sofisoft.Dtos.AccAcountDtos.UpdateAccAccountRequest;
import ma.sofisoft.Exceptions.ErrorResponse;
import ma.sofisoft.Services.AccAccountService;
import ma.sofisoft.Services.SecurityUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/ACC-API/ACCOUNTS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "ACCOUNT API", description = "Management of accounting accounts")
@ApplicationScoped
public class AccAccountResource {
    @Inject
    AccAccountService accAccountService;
    @Inject
    SecurityUtils securityUtils;

    @POST
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Create an account", description = "Create a new accounting account")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Compte créé avec succès"),
            @APIResponse(responseCode = "400", description = "Données de compte invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "Compte parent introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "Le code de compte existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response createAccount(CreateAccAccountRequest dto) {
        String createdBy = securityUtils.getCurrentUserName();
        return Response.ok(accAccountService.createAccount(dto, createdBy)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Update an account", description = "Updates an existing account")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Compte mis à jour avec succès"),
            @APIResponse(responseCode = "400", description = "Données de compte invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "Compte ou compte parent introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "Le code de compte existe déjà ou la relation parentale n'est pas valide",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response updateAccount(@PathParam("id") Long id, UpdateAccAccountRequest dto) {
        String updatedBy = securityUtils.getCurrentUserName();
        return Response.ok(accAccountService.updateAccount(id, dto, updatedBy)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Get an account by ID", description = "Returns an account based on its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Compte récupéré avec succès"),
            @APIResponse(responseCode = "404", description = "Compte non trouvé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response getAccountById(@PathParam("id") Long id) {
        return Response.ok(accAccountService.getAccountById(id)).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "List of accounts", description = "Returns the list of all accounts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Comptes récupérés avec succès"),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response getAllAccounts() {
        return Response.ok(accAccountService.getAllAccounts()).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @Operation(summary = "Delete an account", description = "Delete an account based on its ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Compte supprimé avec succès"),
            @APIResponse(responseCode = "404", description = "Compte non trouvé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "409", description = "Impossible de supprimer un compte avec des enfants",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Response deleteAccount(@PathParam("id") Long id) {
        return Response.ok(accAccountService.deleteAccount(id)).build();
    }
}
