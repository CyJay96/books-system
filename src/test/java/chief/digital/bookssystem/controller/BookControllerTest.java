package chief.digital.bookssystem.controller;

import chief.digital.bookssystem.builder.book.BookDtoRequestTestBuilder;
import chief.digital.bookssystem.builder.book.BookDtoResponseTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import static chief.digital.bookssystem.util.TestConstants.TEST_ID;
import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE;
import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Captor
    ArgumentCaptor<BookDtoRequest> bookDtoRequestCaptor;

    private final BookDtoRequest bookDtoRequest = BookDtoRequestTestBuilder.aBookDtoRequest().build();
    private final BookDtoResponse expectedBookDtoResponse = BookDtoResponseTestBuilder.aBookDtoResponse().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        bookController = new BookController(bookService);
    }

    @Nested
    public class SaveByLibraryIdTest {
        @Test
        @DisplayName("Save Book by Library ID")
        void checkSaveByLibraryIdShouldReturnBookDtoResponse() {
            doReturn(expectedBookDtoResponse).when(bookService).saveByLibraryId(TEST_ID, bookDtoRequest);

            var actualBook = bookController.saveByLibraryId(TEST_ID, bookDtoRequest);

            verify(bookService).saveByLibraryId(anyLong(), any());

            assertAll(
                    () -> assertThat(actualBook.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(actualBook.getBody()).getData())
                            .isEqualTo(expectedBookDtoResponse)
            );
        }

        @Test
        @DisplayName("Save Book by Library ID with Argument Captor")
        void checkSaveByNewsIdWithArgumentCaptorShouldReturnBookDtoResponse() {
            doReturn(expectedBookDtoResponse).when(bookService).saveByLibraryId(TEST_ID, bookDtoRequest);

            bookController.saveByLibraryId(TEST_ID, bookDtoRequest);

            verify(bookService).saveByLibraryId(anyLong(), bookDtoRequestCaptor.capture());

            assertThat(bookDtoRequestCaptor.getValue()).isEqualTo(bookDtoRequest);
        }
    }

    @Test
    @DisplayName("Find all Books")
    void checkFindAllShouldReturnBookPage() {
        PageResponse<BookDtoResponse> pageResponse = PageResponse.<BookDtoResponse>builder()
                .content(List.of(expectedBookDtoResponse))
                .number(TEST_PAGE)
                .size(TEST_PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(bookService).findAll(pageable);

        var actualBooks = bookController.findAll(pageable);

        verify(bookService).findAll(any());

        assertAll(
                () -> assertThat(actualBooks.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualBooks.getBody()).getData().getContent().stream()
                        .anyMatch(actualBookDtoResponse -> actualBookDtoResponse.equals(expectedBookDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnBookDtoResponse(Long id) {
            doReturn(expectedBookDtoResponse).when(bookService).findById(id);

            var actualBook = bookController.findById(id);

            verify(bookService).findById(anyLong());

            assertAll(
                    () -> assertThat(actualBook.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualBook.getBody()).getData())
                            .isEqualTo(expectedBookDtoResponse)
            );
        }

        @Test
        @DisplayName("Find by ID; not found")
        void checkFindByIdShouldThrowBookNotFoundException() {
            doThrow(EntityNotFoundException.class).when(bookService).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> bookController.findById(TEST_ID));

            verify(bookService).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnBookDtoResponse(Long id) {
            doReturn(expectedBookDtoResponse).when(bookService).update(id, bookDtoRequest);

            var actualBook = bookController.update(id, bookDtoRequest);

            verify(bookService).update(anyLong(), any());

            assertAll(
                    () -> assertThat(actualBook.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualBook.getBody()).getData())
                            .isEqualTo(expectedBookDtoResponse)
            );
        }

        @DisplayName("Update Book by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnBookDtoResponse(Long id) {
            doReturn(expectedBookDtoResponse).when(bookService).update(id, bookDtoRequest);

            bookController.update(id, bookDtoRequest);

            verify(bookService).update(anyLong(), bookDtoRequestCaptor.capture());

            assertThat(bookDtoRequestCaptor.getValue()).isEqualTo(bookDtoRequest);
        }

        @Test
        @DisplayName("Update Book by ID; not found")
        void checkUpdateShouldThrowBookNotFoundException() {
            doThrow(EntityNotFoundException.class).when(bookService).update(anyLong(), any());

            assertThrows(EntityNotFoundException.class,
                    () -> bookController.update(TEST_ID, bookDtoRequest)
            );

            verify(bookService).update(anyLong(), any());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            doNothing().when(bookService).deleteById(id);

            var voidResponse = bookController.deleteById(id);

            verify(bookService).deleteById(anyLong());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete Book by ID; not found")
        void checkDeleteByIdShouldThrowBookNotFoundException() {
            doThrow(EntityNotFoundException.class).when(bookService).deleteById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> bookController.deleteById(TEST_ID));

            verify(bookService).deleteById(anyLong());
        }
    }
}
