package job_spring_backend.feature.candidate.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateProfileRequest {

    @NotBlank
    private String fullName;

    private String phone;

    private String gender;

    private String address;

    private String resumeUrl;
}