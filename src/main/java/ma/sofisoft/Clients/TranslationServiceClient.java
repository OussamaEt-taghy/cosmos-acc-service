package ma.sofisoft.Clients;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ma.sofisoft.Dtos.TranslationDtos.CreateTranslationDataRequest;
import ma.sofisoft.Dtos.TranslationDtos.ResponseTranslationDataDto;
import ma.sofisoft.Dtos.TranslationDtos.UpdateTranslationDataRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@Path("/TRLS-API")
@RegisterRestClient(configKey = "cosmos-trsl-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TranslationServiceClient {
     // Crée une nouvelle traduction
    @POST
    ResponseTranslationDataDto createTranslation(CreateTranslationDataRequest request);

    // Met à jour une traduction existante
    @PUT
    @Path("/{id}")
    ResponseTranslationDataDto updateTranslation(@PathParam("id") Long id, UpdateTranslationDataRequest request);

    // Récupère une traduction par ID
    @GET
    @Path("/{id}")
    ResponseTranslationDataDto getTranslationById(@PathParam("id") Long id);

// Récupère une traduction par contrainte unique
    @GET
    @Path("/search")
    ResponseTranslationDataDto getTranslationByConstraint(
            @QueryParam("tableName") String tableName,
            @QueryParam("keyId") Integer keyId,
            @QueryParam("columnName") String columnName,
            @QueryParam("localeId") String localeId);

    // Récupère toutes les traductions d'un enregistrement
    @GET
    @Path("/record")
    List<ResponseTranslationDataDto> getTranslationsForRecord(
            @QueryParam("tableName") String tableName,
            @QueryParam("keyId") Integer keyId);

    // Récupère toutes les traductions d'un enregistrement pour une locale spécifique
    @GET
    @Path("/record/locale")
    List<ResponseTranslationDataDto> getTranslationsForRecordAndLocale(
            @QueryParam("tableName") String tableName,
            @QueryParam("keyId") Integer keyId,
            @QueryParam("localeId") String localeId);


     // Supprime une traduction par ID
    @DELETE
    @Path("/{id}")
    String deleteTranslation(@PathParam("id") Long id);

    // Supprime toutes les traductions d'un enregistrement
    @DELETE
    @Path("/record")
    String deleteTranslationsForRecord(
            @QueryParam("tableName") String tableName,
            @QueryParam("keyId") Integer keyId);

     // Créer plusieurs traductions en lot
    @POST
    @Path("/batch")
    List<ResponseTranslationDataDto> createBatchTranslations(List<CreateTranslationDataRequest> requests);
}
