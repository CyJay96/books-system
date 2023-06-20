package chief.digital.bookssystem.builder.library;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.entity.Book;
import chief.digital.bookssystem.model.entity.Library;
import chief.digital.bookssystem.model.entity.User;
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
@NoArgsConstructor(staticName = "aLibrary")
public class LibraryTestBuilder implements TestBuilder<Library> {

    private Long id = TEST_ID;

    private String title = TEST_STRING;

    private String description = TEST_STRING;

    private String city = TEST_STRING;

    private LocalTime openingTime = TEST_TIME;

    private LocalTime closingTime = TEST_TIME;

    private List<Book> books = new ArrayList<>();

    private List<User> users = new ArrayList<>();

    @Override
    public Library build() {
        return Library.builder()
                .id(id)
                .title(title)
                .description(description)
                .city(city)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .books(books)
                .users(users)
                .build();
    }
}
