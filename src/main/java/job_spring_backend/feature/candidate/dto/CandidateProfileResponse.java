package job_spring_backend.feature.candidate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateProfileResponse {

    private Long id;

    private String keycloakUserId;

    private String email;

    private String fullName;

    private String phone;

    private String location;

    private String currentPosition;

    private Double expectedSalary;

    private String profilePhotoUrl;

    private String role;
}