package job_spring_backend.feature.candidate;
import job_spring_backend.feature.candidate.dto.request.UpdateCandidateProfileRequest;
import job_spring_backend.feature.candidate.dto.response.CandidateProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

//    @GetMapping("/me")
//    public CandidateProfileResponse getMyProfile(
//            @AuthenticationPrincipal Jwt jwt
//    ) {
//        return candidateService.getOrCreateProfile(jwt);
//    }
//
//    @PutMapping("/me")
//    public CandidateProfileResponse updateMyProfile(
//            @AuthenticationPrincipal Jwt jwt,
//            @RequestBody UpdateCandidateProfileRequest request
//    ) {
//        return candidateService.updateProfile(jwt, request);
//    }

    @GetMapping("/me")
    public CandidateProfileResponse getMyProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return candidateService.getMyProfile(jwt);
    }

    @PostMapping("/me")
    public CandidateProfileResponse createMyProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return candidateService.createMyProfile(jwt);
    }

    @PutMapping("/me")
    public CandidateProfileResponse updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateCandidateProfileRequest request
    ) {
        return candidateService.updateProfile(jwt, request);
    }



}