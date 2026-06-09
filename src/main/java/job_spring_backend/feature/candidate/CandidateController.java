package job_spring_backend.feature.candidate;
import jakarta.validation.Valid;
import job_spring_backend.domain.Candidate;
import job_spring_backend.feature.candidate.dto.request.CandidateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<?> createCandidateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CandidateProfileRequest request
    ) {
        Candidate candidate = candidateService.createCandidateProfile(jwt, request);

        return ResponseEntity.ok(candidate);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Candidate candidate = candidateService.getMyCandidateProfile(jwt);

        return ResponseEntity.ok(candidate);
    }
}
