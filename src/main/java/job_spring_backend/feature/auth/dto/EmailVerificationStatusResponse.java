package job_spring_backend.feature.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailVerificationStatusResponse {

    private String email;

    private boolean verified;

    private String message;
}
