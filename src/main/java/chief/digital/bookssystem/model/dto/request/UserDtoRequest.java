package chief.digital.bookssystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import static chief.digital.bookssystem.util.Constants.EMAIL_REGEX;

@Data
@Builder
@Schema(description = "User DTO Request")
public class UserDtoRequest {

    @NotBlank(message = "Username cannot be empty")
    @Length(max = 255, message = "Username is too long")
    @JsonProperty(value = "username")
    private String username;

    @Length(max = 255, message = "User first name is too long")
    @JsonProperty(value = "firstName")
    private String firstName;

    @Length(max = 255, message = "User last name is too long")
    @JsonProperty(value = "lastName")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Length(max = 255, message = "User email is too long")
    @Pattern(regexp = EMAIL_REGEX, message = "Incorrect email address")
    @JsonProperty(value = "email")
    private String email;
}
