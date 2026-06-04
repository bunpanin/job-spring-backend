package job_spring_backend.feature;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublicJobController {

    @GetMapping("/api/public/jobs")
    public List<String> getPublicJobs() {
        return List.of(
                "Java Spring Boot Developer",
                "Frontend React Developer",
                "DevOps Engineer"
        );
    }
}