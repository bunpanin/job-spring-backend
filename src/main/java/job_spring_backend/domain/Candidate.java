package job_spring_backend.domain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "candidates",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "keycloak_user_id"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_user_id", nullable = false, unique = true)
    private String keycloakUserId;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;

    private String phone;

    private String gender;

    private String address;

    private String resumeUrl;

    private String status;
}