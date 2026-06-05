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

    public boolean isEmailVerified(String email) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .searchByEmail(email, true);

        if (users.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserRepresentation user = users.get(0);

        return Boolean.TRUE.equals(user.isEmailVerified());
    }

    public void resendVerificationEmail(String email) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .searchByEmail(email, true);

        if (users.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserRepresentation user = users.get(0);

        if (Boolean.TRUE.equals(user.isEmailVerified())) {
            throw new RuntimeException("Email is already verified");
        }

        keycloak.realm(realm)
                .users()
                .get(user.getId())
                .sendVerifyEmail();
    }

    public String registerCandidate(RegisterCandidateRequest request) {

        List<UserRepresentation> existingUsers = keycloak.realm(realm)
                .users()
                .searchByEmail(request.getEmail(), true);

        if (!existingUsers.isEmpty()) {
            UserRepresentation existingUser = existingUsers.get(0);

            if (!Boolean.TRUE.equals(existingUser.isEmailVerified())) {
                keycloak.realm(realm)
                        .users()
                        .get(existingUser.getId())
                        .sendVerifyEmail();

                return "This email is already registered but not verified. We sent verification email again.";
            }

            throw new RuntimeException("This email is already registered. Please login.");
        }

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

        return "Register successful. Please check your email to verify account.";
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