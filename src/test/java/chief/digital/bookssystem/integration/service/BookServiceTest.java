package chief.digital.bookssystem.integration.service;

import chief.digital.bookssystem.builder.book.BookDtoRequestTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.integration.BaseIntegrationTest;
import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.repository.BookRepository;
import chief.digital.bookssystem.repository.LibraryRepository;
import chief.digital.bookssystem.service.BookService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Random;

import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE;
import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookServiceTest extends BaseIntegrationTest {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final EntityManager entityManager;

    private final BookDtoRequest bookDtoRequest = BookDtoRequestTestBuilder.aBookDtoRequest().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @Test
    @DisplayName("Save Book by Library ID")
    void checkSaveByLibraryIdShouldReturnBookDtoResponse() {
        Long expectedLibraryId = libraryRepository.findFirstByOrderByIdAsc().get().getId();
        BookDtoResponse actualBook = bookService.saveByLibraryId(expectedLibraryId, bookDtoRequest);
        assertThat(actualBook.getLibraryId()).isEqualTo(expectedLibraryId);
    }

    @Test
    @DisplayName("Find all Books")
    void checkFindAllShouldReturnBookDtoResponsePage() {
        int expectedBooksSize = (int) bookRepository.count();
        PageResponse<BookDtoResponse> actualBooks = bookService.findAll(pageable);
        assertThat(actualBooks.getContent()).hasSize(expectedBooksSize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkFindByIdShouldReturnBookDtoResponse(Long id) {
            BookDtoResponse actualBook = bookService.findById(id);
            assertThat(actualBook.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find Book by ID; not found")
        void checkFindByIdShouldThrowBookNotFoundException() {
            Long doesntExistBookId = new Random()
                    .nextLong(bookRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> bookService.findById(doesntExistBookId));
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkUpdateShouldReturnBookDtoResponse(Long id) {
            BookDtoResponse actualBook = bookService.update(id, bookDtoRequest);
            assertThat(actualBook.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update Book by ID; not found")
        void checkUpdateShouldThrowBookNotFoundException() {
            Long doesntExistBookId = new Random()
                    .nextLong(bookRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> bookService.update(doesntExistBookId, bookDtoRequest)
            );
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            bookService.deleteById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete Book by ID; not found")
        void checkDeleteByIdShouldThrowBookNotFoundException() {
            Long doesntExistBookId = new Random()
                    .nextLong(bookRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> {
                bookService.deleteById(doesntExistBookId);
                entityManager.flush();
            });
        }
    }
}
