package chief.digital.bookssystem.integration.controller;

import chief.digital.bookssystem.builder.library.LibraryDtoRequestTestBuilder;
import chief.digital.bookssystem.integration.BaseIntegrationTest;
import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.repository.LibraryRepository;
import chief.digital.bookssystem.repository.UserRepository;
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

import static chief.digital.bookssystem.controller.LibraryController.LIBRARY_API_PATH;
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
class LibraryControllerTest extends BaseIntegrationTest {

    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;

    private final LibraryDtoRequest libraryDtoRequest = LibraryDtoRequestTestBuilder
            .aLibraryDtoRequest()
            .build();

    @Test
    @DisplayName("Save Library")
    void checkSaveShouldReturnLibraryDtoResponse() throws Exception {
        mockMvc.perform(post(LIBRARY_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.title").value(libraryDtoRequest.getTitle()));
    }

    @Test
    @DisplayName("Find all Library")
    void checkFindAllShouldReturnLibraryDtoResponsePage() throws Exception {
        int expectedLibrarySize = (int) libraryRepository.count();
        mockMvc.perform(get(LIBRARY_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(PAGE_PARAM, String.valueOf(TEST_PAGE))
                        .param(SIZE_PARAM, String.valueOf(TEST_PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedLibrarySize));
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkFindByIdShouldReturnLibraryDtoResponse(Long id) throws Exception {
            mockMvc.perform(get(LIBRARY_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param(PAGE_PARAM, String.valueOf(TEST_PAGE))
                            .param(SIZE_PARAM, String.valueOf(TEST_PAGE_SIZE)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Find Library by ID; not found")
        void checkFindByIdShouldThrowLibraryNotFoundException() throws Exception {
            long doesntExistLibraryId = new Random()
                    .nextLong(libraryRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(LIBRARY_API_PATH + "/{id}", doesntExistLibraryId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param(PAGE_PARAM, String.valueOf(TEST_PAGE))
                            .param(SIZE_PARAM, String.valueOf(TEST_PAGE_SIZE)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkUpdateShouldReturnLibraryDtoResponse(Long id) throws Exception {
            mockMvc.perform(put(LIBRARY_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(libraryDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @DisplayName("Partial Update Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkUpdatePartiallyShouldReturnLibraryDtoResponse(Long id) throws Exception {
            mockMvc.perform(patch(LIBRARY_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(libraryDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Update Library by ID; not found")
        void checkUpdateShouldThrowLibraryNotFoundException() throws Exception {
            long doesntExistLibraryId = new Random()
                    .nextLong(libraryRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(put(LIBRARY_API_PATH + "/{id}", doesntExistLibraryId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(libraryDtoRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Partial Update Library by ID; not found")
        void checkUpdatePartiallyShouldThrowLibraryNotFoundException() throws Exception {
            long doesntExistLibraryId = new Random()
                    .nextLong(libraryRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(patch(LIBRARY_API_PATH + "/{id}", doesntExistLibraryId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(libraryDtoRequest)))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("Add User to Library by User ID")
    void checkAddUserByUserIdShouldReturnLibraryDtoResponse() throws Exception {
        Long existsLibraryId = libraryRepository.findFirstByOrderByIdAsc().get().getId();
        Long existsUserId = userRepository.findFirstByOrderByIdDesc().get().getId();
        mockMvc.perform(patch(LIBRARY_API_PATH + "/addUser/{existsLibraryId}/{existsUserId}",
                        existsLibraryId, existsUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete User from Library by User ID")
    void checkDeleteUserByUserIdShouldReturnLibraryDtoResponse() throws Exception {
        Long existsLibraryId = libraryRepository.findFirstByOrderByIdAsc().get().getId();
        Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
        mockMvc.perform(patch(LIBRARY_API_PATH + "/deleteUser/{existsLibraryId}/{existsUserId}",
                        existsLibraryId, existsUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkDeleteByIdShouldReturnLibraryDtoResponse(Long id) throws Exception {
            mockMvc.perform(delete(LIBRARY_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Delete Library by ID; not found")
        void checkDeleteByIdShouldThrowLibraryNotFoundException() throws Exception {
            long doesntExistLibraryId = new Random()
                    .nextLong(libraryRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(delete(LIBRARY_API_PATH + "/{id}", doesntExistLibraryId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
