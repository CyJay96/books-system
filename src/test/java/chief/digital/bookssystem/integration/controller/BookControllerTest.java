package chief.digital.bookssystem.integration.controller;

import chief.digital.bookssystem.builder.book.BookDtoRequestTestBuilder;
import chief.digital.bookssystem.integration.BaseIntegrationTest;
import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.repository.BookRepository;
import chief.digital.bookssystem.repository.LibraryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static chief.digital.bookssystem.controller.BookController.BOOK_API_PATH;
import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE;
import static chief.digital.bookssystem.util.TestConstants.TEST_PAGE_SIZE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookControllerTest extends BaseIntegrationTest {

    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    private final BookDtoRequest bookDtoRequest = BookDtoRequestTestBuilder
            .aBookDtoRequest()
            .build();

    @Test
    @DisplayName("Save Book")
    void checkSaveShouldReturnBookDtoResponse() throws Exception {
        Long expectedLibraryId = libraryRepository.findFirstByOrderByIdAsc().get().getId();
        mockMvc.perform(post(BOOK_API_PATH + "/{libraryId}", expectedLibraryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.libraryId").value(expectedLibraryId));
    }

    @Test
    @DisplayName("Find all Books")
    void checkFindAllShouldReturnBookDtoResponsePage() throws Exception {
        int expectedBooksSize = (int) bookRepository.count();
        mockMvc.perform(get(BOOK_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(PAGE_PARAM, String.valueOf(TEST_PAGE))
                        .param(SIZE_PARAM, String.valueOf(TEST_PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedBooksSize));
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkFindByIdShouldReturnBookDtoResponse(Long id) throws Exception {
            mockMvc.perform(get(BOOK_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Find Book by ID; not found")
        void checkFindByIdShouldThrowBookNotFoundException() throws Exception {
            long doesntExistBookId = new Random()
                    .nextLong(bookRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(BOOK_API_PATH + "/{id}", doesntExistBookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class UpdateByIdTest {
        @DisplayName("Update Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkUpdateShouldReturnBookDtoResponse(Long id) throws Exception {
            mockMvc.perform(put(BOOK_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @DisplayName("Partial Update Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkUpdatePartiallyShouldReturnBookDtoResponse(Long id) throws Exception {
            mockMvc.perform(patch(BOOK_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Update Book by ID; not found")
        void checkUpdateShouldThrowBookNotFoundException() throws Exception {
            long doesntExistBookId = new Random()
                    .nextLong(bookRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(put(BOOK_API_PATH + "/{id}", doesntExistBookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDtoRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Partial Update Book by ID; not found")
        void checkUpdatePartiallyShouldThrowBookNotFoundException() throws Exception {
            long doesntExistBookId = new Random()
                    .nextLong(bookRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(patch(BOOK_API_PATH + "/{id}", doesntExistBookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookDtoRequest)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Book by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkDeleteByIdShouldReturnVoid(Long id) throws Exception {
            mockMvc.perform(delete(BOOK_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Delete Book by ID; not found")
        void checkDeleteByIdShouldThrowBookNotFoundException() throws Exception {
            long doesntExistBookId = new Random()
                    .nextLong(bookRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(delete(BOOK_API_PATH + "/{id}", doesntExistBookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
