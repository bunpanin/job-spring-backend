package job_spring_backend.feature.auth;
import job_spring_backend.feature.auth.dto.AuthResponse;
import job_spring_backend.feature.auth.dto.EmailVerificationStatusResponse;
import job_spring_backend.feature.auth.dto.RegisterCandidateRequest;
import job_spring_backend.feature.auth.dto.ResendVerificationEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakUserService keycloakUserService;

    @GetMapping("/check-email-verified")
    public EmailVerificationStatusResponse checkEmailVerified(
            @RequestParam String email
    ) {
        boolean verified = keycloakUserService.isEmailVerified(email);

        return new EmailVerificationStatusResponse(
                email,
                verified,
                verified ? "Your account has been verified." : "Your account is not verified yet."
        );
    }

    @PostMapping("/register-candidate")
    public AuthResponse registerCandidate(@RequestBody RegisterCandidateRequest request) {
        keycloakUserService.registerCandidate(request);

        return new AuthResponse(
                "Register successful. Please check your email to verify account."
        );
    }

    @PostMapping("/resend-verification-email")
    public AuthResponse resendVerificationEmail(
            @RequestBody ResendVerificationEmailRequest request
    ) {
        keycloakUserService.resendVerificationEmail(request.getEmail());

        return new AuthResponse("Verification email sent again. Please check your email.");
    }
}