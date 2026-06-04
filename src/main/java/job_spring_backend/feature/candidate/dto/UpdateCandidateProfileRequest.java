package job_spring_backend.feature.candidate.dto;
import lombok.Data;

@Data
public class UpdateCandidateProfileRequest {

    private String fullName;

    private String phone;

    private String location;

    private String currentPosition;

    private Double expectedSalary;

    private String profilePhotoUrl;
}
