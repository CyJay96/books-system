package chief.digital.bookssystem.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Data
@Builder
@Schema(description = "Library DTO Request")
public class LibraryDtoRequest {

    @Length(max = 255, message = "Title is too long")
    @NotBlank(message = "Title cannot be empty")
    @JsonProperty(value = "title")
    private String title;

    @Length(max = 255, message = "Description is too long")
    @NotBlank(message = "Description cannot be empty")
    @JsonProperty(value = "description")
    private String description;

    @Length(max = 255, message = "City is too long")
    @NotBlank(message = "City cannot be empty")
    @JsonProperty(value = "city")
    private String city;

    @NotNull(message = "Opening Time cannot be null")
    @JsonProperty(value = "openingTime")
    private LocalTime openingTime;

    @NotNull(message = "Closing Time cannot be null")
    @JsonProperty(value = "closingTime")
    private LocalTime closingTime;
}
