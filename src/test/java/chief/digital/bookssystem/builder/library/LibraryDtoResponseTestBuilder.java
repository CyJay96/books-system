package chief.digital.bookssystem.builder.library;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static chief.digital.bookssystem.util.TestConstants.TEST_ID;
import static chief.digital.bookssystem.util.TestConstants.TEST_STRING;
import static chief.digital.bookssystem.util.TestConstants.TEST_TIME;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aLibraryDtoResponse")
public class LibraryDtoResponseTestBuilder implements TestBuilder<LibraryDtoResponse> {

    private Long id = TEST_ID;

    private String title = TEST_STRING;

    private String description = TEST_STRING;

    private String city = TEST_STRING;

    private LocalTime openingTime = TEST_TIME;

    private LocalTime closingTime = TEST_TIME;

    private List<Long> booksIds = new ArrayList<>();

    private List<Long> usersIds = new ArrayList<>();

    @Override
    public LibraryDtoResponse build() {
        return LibraryDtoResponse.builder()
                .id(id)
                .title(title)
                .description(description)
                .city(city)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .booksIds(booksIds)
                .usersIds(usersIds)
                .build();
    }
}
