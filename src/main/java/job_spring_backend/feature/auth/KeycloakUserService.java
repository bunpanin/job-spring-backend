package job_spring_backend.feature.auth;
import jakarta.ws.rs.core.Response;
import job_spring_backend.feature.auth.dto.RegisterCandidateRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Keycloak keycloak;

    private final String realm = "job-spring";

    public void registerCandidate(RegisterCandidateRequest request) {
        UserRepresentation user = new UserRepresentation();

        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRequiredActions(List.of("VERIFY_EMAIL"));

        CredentialRepresentation password = new CredentialRepresentation();
        password.setTemporary(false);
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(request.getPassword());

        user.setCredentials(List.of(password));

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Cannot create candidate. Status: " + response.getStatus());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        assignCandidateRole(userId);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .sendVerifyEmail();
    }

    private void assignCandidateRole(String userId) {
        RoleRepresentation candidateRole = keycloak.realm(realm)
                .roles()
                .get("CANDIDATE")
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(candidateRole));
    }
}