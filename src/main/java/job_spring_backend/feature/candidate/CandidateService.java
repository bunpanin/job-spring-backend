package job_spring_backend.feature.candidate;

import job_spring_backend.domain.Candidate;
import job_spring_backend.feature.candidate.dto.request.CandidateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Transactional
    public Candidate createCandidateProfile(Jwt jwt, CandidateProfileRequest request) {

        String keycloakUserId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        Boolean emailVerified = jwt.getClaimAsBoolean("email_verified");

        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            throw new RuntimeException("Invalid Keycloak user ID");
        }

        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email not found in Keycloak token");
        }

//        if (!Boolean.TRUE.equals(emailVerified)) {
//            throw new RuntimeException("Please verify your email before creating candidate profile");
//        }
//
//        if (candidateRepository.existsByKeycloakUserId(keycloakUserId)) {
//            throw new RuntimeException("Candidate profile already exists for this user");
//        }
//
//        if (candidateRepository.existsByEmail(email)) {
//            throw new RuntimeException("Candidate email already exists");
//        }

        Candidate candidate = Candidate.builder()
                .keycloakUserId(keycloakUserId)
                .email(email)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .gender(request.getGender())
                .address(request.getAddress())
                .resumeUrl(request.getResumeUrl())
                .status("ACTIVE")
                .build();

        return candidateRepository.save(candidate);
    }

    public Candidate getMyCandidateProfile(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();

        return candidateRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new RuntimeException("Candidate profile not found"));
    }
}