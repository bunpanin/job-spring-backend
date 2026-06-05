package job_spring_backend.feature.auth;
import job_spring_backend.feature.auth.dto.AuthResponse;
import job_spring_backend.feature.auth.dto.RegisterCandidateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakUserService keycloakUserService;

    @PostMapping("/register-candidate")
    public AuthResponse registerCandidate(@RequestBody RegisterCandidateRequest request) {
        keycloakUserService.registerCandidate(request);

        return new AuthResponse(
                "Register successful. Please check your email to verify account."
        );
    }
}