package job_spring_backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // ✅ Get current logged-in user
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "username", jwt.getClaim("preferred_username"),
                "email",    jwt.getClaim("email"),
                "name",     jwt.getClaim("name"),
                "roles",    jwt.getClaim("realm_access")
        );
    }

    // ✅ Protected endpoint example
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Jwt jwt) {
        return "Welcome " + jwt.getClaim("preferred_username");
    }
}