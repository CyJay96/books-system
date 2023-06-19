package chief.digital.bookssystem.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@Schema(description = "Library DTO Response")
public class LibraryDtoResponse {

    private Long id;

    private String title;

    private String description;

    private String city;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private List<Long> booksIds;

    private List<Long> usersIds;
}
