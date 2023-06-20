package chief.digital.bookssystem.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Book DTO Response")
public class BookDtoResponse {

    private Long id;

    private String title;

    private String description;

    private String author;

    private String genre;

    private Integer publicationYear;

    private Long libraryId;
}
