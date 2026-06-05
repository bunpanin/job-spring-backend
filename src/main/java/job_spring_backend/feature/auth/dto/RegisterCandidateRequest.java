package job_spring_backend.feature.auth.dto;
import lombok.Data;

@Data
public class RegisterCandidateRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}