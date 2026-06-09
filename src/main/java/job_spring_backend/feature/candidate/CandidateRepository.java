package job_spring_backend.feature.candidate;
import job_spring_backend.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    boolean existsByKeycloakUserId(String keycloakUserId);

    boolean existsByEmail(String email);

    Optional<Candidate> findByKeycloakUserId(String keycloakUserId);
}