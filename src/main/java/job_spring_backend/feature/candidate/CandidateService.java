package job_spring_backend.feature.candidate;
import job_spring_backend.domain.Candidate;
import job_spring_backend.feature.candidate.dto.request.UpdateCandidateProfileRequest;
import job_spring_backend.feature.candidate.dto.response.CandidateProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateProfileResponse getMyProfile(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();

        Candidate candidate = candidateRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Candidate do not exist. Please create profile"));

        return toResponse(candidate, jwt);
    }

    public CandidateProfileResponse createMyProfile(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");

        candidateRepository.findByKeycloakUserId(keycloakUserId)
                .ifPresent(candidate -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate profile already exists");
                });

        Candidate candidateByEmail = candidateRepository
                .findByEmail(email)
                .orElse(null);

        if (candidateByEmail != null) {
            candidateByEmail.setKeycloakUserId(keycloakUserId);

            if (candidateByEmail.getFullName() == null || candidateByEmail.getFullName().isBlank()) {
                candidateByEmail.setFullName(name);
            }

            return toResponse(candidateRepository.save(candidateByEmail), jwt);
        }

        Candidate candidate = Candidate.builder()
                .keycloakUserId(keycloakUserId)
                .email(email)
                .fullName(name)
                .build();

        Candidate saved = candidateRepository.save(candidate);

        return toResponse(saved, jwt);
    }

    public CandidateProfileResponse updateProfile(
            Jwt jwt,
            UpdateCandidateProfileRequest request
    ) {
        String keycloakUserId = jwt.getSubject();

        Candidate candidate = candidateRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

        candidate.setFullName(request.getFullName());
        candidate.setPhone(request.getPhone());
        candidate.setLocation(request.getLocation());
        candidate.setCurrentPosition(request.getCurrentPosition());
        candidate.setExpectedSalary(request.getExpectedSalary());
        candidate.setProfilePhotoUrl(request.getProfilePhotoUrl());
        candidate.setSummary(request.getSummary());

        return toResponse(candidateRepository.save(candidate), jwt);
    }


//    public CandidateProfileResponse getOrCreateProfile(Jwt jwt) {
//        String keycloakUserId = jwt.getSubject();
//        String email = jwt.getClaimAsString("email");
//        String name = jwt.getClaimAsString("name");
//
//        Candidate candidate = candidateRepository
//                .findByKeycloakUserId(keycloakUserId)
//                .orElseGet(() -> {
//                    Candidate newCandidate = Candidate.builder()
//                            .keycloakUserId(keycloakUserId)
//                            .email(email)
//                            .fullName(name)
//                            .build();
//
//                    return candidateRepository.save(newCandidate);
//                });
//
//        return toResponse(candidate, jwt);
//    }
//
//    public CandidateProfileResponse updateProfile(
//            Jwt jwt,
//            UpdateCandidateProfileRequest request
//    ) {
//        String keycloakUserId = jwt.getSubject();
//
//        Candidate candidate = candidateRepository
//                .findByKeycloakUserId(keycloakUserId)
//                .orElseThrow(() -> new RuntimeException("Candidate profile not found"));
//
//        candidate.setFullName(request.getFullName());
//        candidate.setPhone(request.getPhone());
//        candidate.setLocation(request.getLocation());
//        candidate.setCurrentPosition(request.getCurrentPosition());
//        candidate.setExpectedSalary(request.getExpectedSalary());
//        candidate.setProfilePhotoUrl(request.getProfilePhotoUrl());
//        candidate.setSummary(request.getSummary());
//
//        Candidate savedCandidate = candidateRepository.save(candidate);
//
//        return toResponse(savedCandidate, jwt);
//    }
//
    private CandidateProfileResponse toResponse(Candidate candidate, Jwt jwt) {
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
                .summary(candidate.getSummary())
                .role(getRoleFromJwt(jwt))
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