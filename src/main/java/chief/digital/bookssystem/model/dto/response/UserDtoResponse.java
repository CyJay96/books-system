package chief.digital.bookssystem.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "User DTO Response")
public class UserDtoResponse {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private List<Long> librariesIds;
}
