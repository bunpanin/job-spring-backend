package job_spring_backend.feature.candidate;
import job_spring_backend.feature.candidate.dto.CandidateProfileResponse;
import job_spring_backend.feature.candidate.dto.UpdateCandidateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping("/me")
    public CandidateProfileResponse getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return candidateService.getOrCreateProfile(jwt);
    }

    @PutMapping("/me")
    public CandidateProfileResponse updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateCandidateProfileRequest request
    ) {
        return candidateService.updateProfile(jwt, request);
    }
}
