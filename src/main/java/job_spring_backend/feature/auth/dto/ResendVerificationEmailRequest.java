package job_spring_backend.feature.auth.dto;

import lombok.Data;

@Data
public class ResendVerificationEmailRequest {
    private String email;
}