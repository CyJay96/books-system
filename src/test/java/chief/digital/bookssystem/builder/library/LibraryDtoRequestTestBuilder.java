package chief.digital.bookssystem.builder.library;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalTime;

import static chief.digital.bookssystem.util.TestConstants.TEST_STRING;
import static chief.digital.bookssystem.util.TestConstants.TEST_TIME;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aLibraryDtoRequest")
public class LibraryDtoRequestTestBuilder implements TestBuilder<LibraryDtoRequest> {

    private String title = TEST_STRING;

    private String description = TEST_STRING;

    private String city = TEST_STRING;

    private LocalTime openingTime = TEST_TIME;

    private LocalTime closingTime = TEST_TIME;

    @Override
    public LibraryDtoRequest build() {
        return LibraryDtoRequest.builder()
                .title(title)
                .description(description)
                .city(city)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .build();
    }
}
