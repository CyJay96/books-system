package chief.digital.bookssystem.service;

import chief.digital.bookssystem.builder.book.BookDtoRequestTestBuilder;
import chief.digital.bookssystem.builder.book.BookDtoResponseTestBuilder;
import chief.digital.bookssystem.builder.book.BookTestBuilder;
import chief.digital.bookssystem.builder.library.LibraryTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.mapper.BookMapper;
import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.entity.Book;
import chief.digital.bookssystem.model.entity.Library;
import chief.digital.bookssystem.repository.BookRepository;
import chief.digital.bookssystem.repository.LibraryRepository;
import chief.digital.bookssystem.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static chief.digital.bookssystem.util.TestConstants.TEST_ID;
import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE;
import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private BookMapper bookMapper;

    @Captor
    ArgumentCaptor<Book> bookCaptor;

    private final BookDtoRequest bookDtoRequest = BookDtoRequestTestBuilder.aBookDtoRequest().build();
    private final BookDtoResponse expectedBookDtoResponse = BookDtoResponseTestBuilder.aBookDtoResponse().build();
    private final Book expectedBook = BookTestBuilder.aBook().build();
    private final Library library = LibraryTestBuilder.aLibrary().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, libraryRepository, bookMapper);
    }

    @Nested
    public class SaveByLibraryIdTest {
        @Test
        @DisplayName("Save Book by Library ID")
        void checkSaveByLibraryIdShouldReturnBookDtoResponse() {
            doReturn(Optional.of(library)).when(libraryRepository).findById(TEST_ID);
            doReturn(expectedBook).when(bookMapper).toBook(bookDtoRequest);
            doReturn(expectedBook).when(bookRepository).save(expectedBook);
            doReturn(expectedBookDtoResponse).when(bookMapper).toBookDtoResponse(expectedBook);

            BookDtoResponse actualBook = bookService.saveByLibraryId(TEST_ID, bookDtoRequest);

            verify(bookMapper).toBook(any());
            verify(bookRepository).save(any());
            verify(bookMapper).toBookDtoResponse(any());

            assertThat(actualBook).isEqualTo(expectedBookDtoResponse);
        }

        @Test
        @DisplayName("Save Book by Library ID with Argument Captor")
        void checkSaveByLibraryIdWithArgumentCaptorShouldReturnBookDtoResponse() {
            doReturn(Optional.of(library)).when(libraryRepository).findById(TEST_ID);
            doReturn(expectedBook).when(bookMapper).toBook(bookDtoRequest);
            doReturn(expectedBook).when(bookRepository).save(expectedBook);
            doReturn(expectedBookDtoResponse).when(bookMapper).toBookDtoResponse(expectedBook);

            bookService.saveByLibraryId(TEST_ID, bookDtoRequest);

            verify(libraryRepository).findById(anyLong());
            verify(bookMapper).toBook(any());
            verify(bookRepository).save(bookCaptor.capture());
            verify(bookMapper).toBookDtoResponse(any());

            assertThat(bookCaptor.getValue()).isEqualTo(expectedBook);
        }
    }

    @Test
    @DisplayName("Find all Books")
    void checkFindAllShouldReturnBookDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedBook))).when(bookRepository).findAll(pageable);
        doReturn(expectedBookDtoResponse).when(bookMapper).toBookDtoResponse(expectedBook);

        PageResponse<BookDtoResponse> actualBooks = bookService.findAll(pageable);

        verify(bookRepository).findAll(eq(pageable));
        verify(bookMapper).toBookDtoResponse(any());

        assertThat(actualBooks.getContent().stream()
                .anyMatch(actualBookDtoResponse -> actualBookDtoResponse.equals(expectedBookDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnBookDtoResponse(Long id) {
            doReturn(Optional.of(expectedBook)).when(bookRepository).findById(id);
            doReturn(expectedBookDtoResponse).when(bookMapper).toBookDtoResponse(expectedBook);

            BookDtoResponse actualBook = bookService.findById(id);

            verify(bookRepository).findById(anyLong());
            verify(bookMapper).toBookDtoResponse(any());

            assertThat(actualBook).isEqualTo(expectedBookDtoResponse);
        }

        @Test
        @DisplayName("Find Book by ID; not found")
        void checkFindByIdShouldThrowBookNotFoundException() {
            doThrow(EntityNotFoundException.class).when(bookRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> bookService.findById(TEST_ID));

            verify(bookRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnBookDtoResponse(Long id) {
            doReturn(Optional.of(expectedBook)).when(bookRepository).findById(id);
            doNothing().when(bookMapper).updateBook(bookDtoRequest, expectedBook);
            doReturn(expectedBook).when(bookRepository).save(expectedBook);
            doReturn(expectedBookDtoResponse).when(bookMapper).toBookDtoResponse(expectedBook);

            BookDtoResponse actualBook = bookService.update(id, bookDtoRequest);

            verify(bookRepository).findById(anyLong());
            verify(bookMapper).updateBook(any(), any());
            verify(bookRepository).save(any());
            verify(bookMapper).toBookDtoResponse(any());

            assertThat(actualBook).isEqualTo(expectedBookDtoResponse);
        }

        @DisplayName("Update Book by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnBookDtoResponse(Long id) {
            doReturn(Optional.of(expectedBook)).when(bookRepository).findById(id);
            doNothing().when(bookMapper).updateBook(bookDtoRequest, expectedBook);
            doReturn(expectedBook).when(bookRepository).save(expectedBook);
            doReturn(expectedBookDtoResponse).when(bookMapper).toBookDtoResponse(expectedBook);

            bookService.update(id, bookDtoRequest);

            verify(bookRepository).findById(anyLong());
            verify(bookMapper).updateBook(any(), any());
            verify(bookRepository).save(bookCaptor.capture());
            verify(bookMapper).toBookDtoResponse(any());

            assertThat(bookCaptor.getValue()).isEqualTo(expectedBook);
        }

        @Test
        @DisplayName("Update Book by ID; not found")
        void checkUpdateShouldThrowBookNotFoundException() {
            doThrow(EntityNotFoundException.class).when(bookRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> bookService.update(TEST_ID, bookDtoRequest)
            );

            verify(bookRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnBookDtoResponse(Long id) {
            doReturn(true).when(bookRepository).existsById(id);
            doNothing().when(bookRepository).deleteById(id);

            bookService.deleteById(id);

            verify(bookRepository).existsById(anyLong());
            verify(bookRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Delete Book by ID; not found")
        void checkDeleteByIdShouldThrowBookNotFoundException() {
            doReturn(false).when(bookRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(TEST_ID));

            verify(bookRepository).existsById(anyLong());
        }
    }
}
