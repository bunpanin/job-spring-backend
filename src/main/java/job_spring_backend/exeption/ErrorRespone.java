package job_spring_backend.exeption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorRespone<T> {
    private T error;   
}