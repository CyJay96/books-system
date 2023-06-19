package chief.digital.bookssystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@Schema(description = "Book DTO Request")
public class BookDtoRequest {

    @Length(max = 255, message = "Title is too long")
    @NotBlank(message = "Title cannot be empty")
    @JsonProperty(value = "title")
    private String title;

    @Length(max = 255, message = "Description is too long")
    @NotBlank(message = "Description cannot be empty")
    @JsonProperty(value = "description")
    private String description;

    @Length(max = 255, message = "Author is too long")
    @NotBlank(message = "Author cannot be empty")
    @JsonProperty(value = "author")
    private String author;

    @Length(max = 255, message = "Genre is too long")
    @NotBlank(message = "Genre cannot be empty")
    @JsonProperty(value = "genre")
    private String genre;

    @NotNull(message = "Publication Year cannot be null")
    @Positive(message = "Publication Year must be positive")
    @JsonProperty(value = "publicationYear")
    private Integer publicationYear;
}
