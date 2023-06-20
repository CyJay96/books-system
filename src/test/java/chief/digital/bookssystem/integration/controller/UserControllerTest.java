package chief.digital.bookssystem.integration.controller;

import chief.digital.bookssystem.builder.user.UserDtoRequestTestBuilder;
import chief.digital.bookssystem.integration.BaseIntegrationTest;
import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
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

import static chief.digital.bookssystem.controller.UserController.USER_API_PATH;
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
class UserControllerTest extends BaseIntegrationTest {

    private static final String PAGE_PARAM = "page";
    private static final String SIZE_PARAM = "size";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    private final UserDtoRequest userDtoRequest = UserDtoRequestTestBuilder
            .aUserDtoRequest()
            .build();

    @Test
    @DisplayName("Save User")
    void checkSaveShouldReturnUserDtoResponse() throws Exception {
        mockMvc.perform(post(USER_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value(userDtoRequest.getUsername()));
    }

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserDtoResponsePage() throws Exception {
        int expectedUsersSize = (int) userRepository.count();
        mockMvc.perform(get(USER_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(PAGE_PARAM, String.valueOf(TEST_PAGE))
                        .param(SIZE_PARAM, String.valueOf(TEST_PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedUsersSize));
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) throws Exception {
            mockMvc.perform(get(USER_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindByIdShouldThrowUserNotFoundException() throws Exception {
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(USER_API_PATH + "/{id}", doesntExistUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class UpdateByIdTest {
        @DisplayName("Update User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnUserDtoResponse(Long id) throws Exception {
            mockMvc.perform(put(USER_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @DisplayName("Partial Update User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnUserDtoResponse(Long id) throws Exception {
            mockMvc.perform(patch(USER_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Update User by ID; not found")
        void checkUpdateShouldThrowUserNotFoundException() throws Exception {
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(put(USER_API_PATH + "/{id}", doesntExistUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userDtoRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Partial Update User by ID; not found")
        void checkUpdatePartiallyShouldThrowUserNotFoundException() throws Exception {
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(patch(USER_API_PATH + "/{id}", doesntExistUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userDtoRequest)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) throws Exception {
            mockMvc.perform(delete(USER_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Delete User by ID; not found")
        void checkDeleteByIdShouldThrowUserNotFoundException() throws Exception {
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(delete(USER_API_PATH + "/{id}", doesntExistUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
