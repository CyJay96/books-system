package chief.digital.bookssystem.builder.book;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.builder.library.LibraryTestBuilder;
import chief.digital.bookssystem.model.entity.Book;
import chief.digital.bookssystem.model.entity.Library;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import static chief.digital.bookssystem.util.TestConstants.TEST_ID;
import static chief.digital.bookssystem.util.TestConstants.TEST_INT;
import static chief.digital.bookssystem.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aBook")
public class BookTestBuilder implements TestBuilder<Book> {

    private Long id = TEST_ID;

    private String title = TEST_STRING;

    private String description = TEST_STRING;

    private String author = TEST_STRING;

    private String genre = TEST_STRING;

    private Integer publicationYear = TEST_INT;

    private Library library = LibraryTestBuilder.aLibrary().build();

    @Override
    public Book build() {
        return Book.builder()
                .id(id)
                .title(title)
                .description(description)
                .author(author)
                .genre(genre)
                .publicationYear(publicationYear)
                .library(library)
                .build();
    }
}
