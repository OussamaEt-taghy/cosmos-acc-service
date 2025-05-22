package ma.sofisoft.Services;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.Set;

@ApplicationScoped
public class SecurityUtils {
    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    JsonWebToken jwt;
    public String getCurrentUserId() {
        return jwt.getSubject();
    }
    public String getCurrentUserName() {
        return jwt.getClaim("preferred_username");
    }
    public Set<String> getCurrentUserRoles() {
        return securityIdentity.getRoles();
    }
    public boolean hasRole(String role) {
        return securityIdentity.hasRole(role);
    }
    public boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (securityIdentity.hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}
