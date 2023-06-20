package chief.digital.bookssystem.builder.book;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import static chief.digital.bookssystem.util.TestConstants.TEST_INT;
import static chief.digital.bookssystem.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aBookDtoRequest")
public class BookDtoRequestTestBuilder implements TestBuilder<BookDtoRequest> {

    private String title = TEST_STRING;

    private String description = TEST_STRING;

    private String author = TEST_STRING;

    private String genre = TEST_STRING;

    private Integer publicationYear = TEST_INT;

    @Override
    public BookDtoRequest build() {
        return BookDtoRequest.builder()
                .title(title)
                .description(description)
                .author(author)
                .genre(genre)
                .publicationYear(publicationYear)
                .build();
    }
}
