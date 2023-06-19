package chief.digital.bookssystem.builder.book;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import static chief.digital.bookssystem.util.TestConstants.TEST_ID;
import static chief.digital.bookssystem.util.TestConstants.TEST_INT;
import static chief.digital.bookssystem.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aBookDtoResponse")
public class BookDtoResponseTestBuilder implements TestBuilder<BookDtoResponse> {

    private Long id = TEST_ID;

    private String title = TEST_STRING;

    private String description = TEST_STRING;

    private String author = TEST_STRING;

    private String genre = TEST_STRING;

    private Integer publicationYear = TEST_INT;

    private Long libraryId = TEST_ID;

    @Override
    public BookDtoResponse build() {
        return BookDtoResponse.builder()
                .id(id)
                .title(title)
                .description(description)
                .author(author)
                .genre(genre)
                .publicationYear(publicationYear)
                .libraryId(libraryId)
                .build();
    }
}
