package job_spring_backend.feature.candidate;

import job_spring_backend.domain.Candidate;
import job_spring_backend.feature.candidate.dto.CandidateProfileResponse;
import job_spring_backend.feature.candidate.dto.UpdateCandidateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateProfileResponse getOrCreateProfile(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");

        Candidate candidate = candidateRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseGet(() -> {
                    Candidate newCandidate = Candidate.builder()
                            .keycloakUserId(keycloakUserId)
                            .email(email)
                            .fullName(name)
                            .build();

                    return candidateRepository.save(newCandidate);
                });

//        return toResponse(candidate);\
        return toResponse(candidate, jwt);
    }

    public CandidateProfileResponse updateProfile(Jwt jwt, UpdateCandidateProfileRequest request) {
        String keycloakUserId = jwt.getSubject();

        Candidate candidate = candidateRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new RuntimeException("Candidate profile not found"));

        candidate.setFullName(request.getFullName());
        candidate.setPhone(request.getPhone());
        candidate.setLocation(request.getLocation());
        candidate.setCurrentPosition(request.getCurrentPosition());
        candidate.setExpectedSalary(request.getExpectedSalary());
        candidate.setProfilePhotoUrl(request.getProfilePhotoUrl());

        Candidate saved = candidateRepository.save(candidate);

//        return toResponse(saved);
        return toResponse(saved, jwt);
    }


//    private CandidateProfileResponse toResponse(Candidate candidate) {
//        return CandidateProfileResponse.builder()
//                .id(candidate.getId())
//                .keycloakUserId(candidate.getKeycloakUserId())
//                .email(candidate.getEmail())
//                .fullName(candidate.getFullName())
//                .phone(candidate.getPhone())
//                .location(candidate.getLocation())
//                .currentPosition(candidate.getCurrentPosition())
//                .expectedSalary(candidate.getExpectedSalary())
//                .profilePhotoUrl(candidate.getProfilePhotoUrl())
//                .build();
//    }

    private CandidateProfileResponse toResponse(Candidate candidate, Jwt jwt) {
        String role = getRoleFromJwt(jwt);

        return CandidateProfileResponse.builder()
                .id(candidate.getId())
                .keycloakUserId(candidate.getKeycloakUserId())
                .email(candidate.getEmail())
                .fullName(candidate.getFullName())
                .phone(candidate.getPhone())
                .location(candidate.getLocation())
                .currentPosition(candidate.getCurrentPosition())
                .expectedSalary(candidate.getExpectedSalary())
                .profilePhotoUrl(candidate.getProfilePhotoUrl())
                .role(role)
                .build();
    }
    private String getRoleFromJwt(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null) {
            return null;
        }

        Object rolesObject = realmAccess.get("roles");

        if (!(rolesObject instanceof Collection<?> roles)) {
            return null;
        }

        if (roles.contains("CANDIDATE")) {
            return "CANDIDATE";
        }

        if (roles.contains("EMPLOYER")) {
            return "EMPLOYER";
        }

        if (roles.contains("ADMIN")) {
            return "ADMIN";
        }

        return null;
    }
}
