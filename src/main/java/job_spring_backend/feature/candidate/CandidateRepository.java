package job_spring_backend.feature.candidate;
import job_spring_backend.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Optional<Candidate> findByKeycloakUserId(String keycloakUserId);

    boolean existsByKeycloakUserId(String keycloakUserId);
}