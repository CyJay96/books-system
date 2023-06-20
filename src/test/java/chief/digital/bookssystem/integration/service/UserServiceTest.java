package chief.digital.bookssystem.integration.service;

import chief.digital.bookssystem.builder.user.UserDtoRequestTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.integration.BaseIntegrationTest;
import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import chief.digital.bookssystem.repository.UserRepository;
import chief.digital.bookssystem.service.UserService;
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
class UserServiceTest extends BaseIntegrationTest {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    private final UserDtoRequest userDtoRequest = UserDtoRequestTestBuilder.aUserDtoRequest().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @Test
    @DisplayName("Save User")
    void checkSaveShouldReturnUserDtoResponse() {
        UserDtoResponse actualUser = userService.save(userDtoRequest);
        assertThat(actualUser.getUsername()).isEqualTo(userDtoRequest.getUsername());
    }

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserDtoResponsePage() {
        int expectedUsersSize = (int) userRepository.count();
        PageResponse<UserDtoResponse> actualUsers = userService.findAll(pageable);
        assertThat(actualUsers.getContent()).hasSize(expectedUsersSize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) {
            UserDtoResponse actualUser = userService.findById(id);
            assertThat(actualUser.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindByIdShouldThrowUserNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> userService.findById(doesntExistUserId));
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkUpdateShouldReturnUserDtoResponse(Long id) {
            UserDtoResponse actualUser = userService.update(id, userDtoRequest);
            assertThat(actualUser.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update User by ID; not found")
        void checkUpdateShouldThrowUserNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> userService.update(doesntExistUserId, userDtoRequest)
            );
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            userService.deleteById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete User by ID; not found")
        void checkDeleteByIdShouldThrowUserNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> {
                userService.deleteById(doesntExistUserId);
                entityManager.flush();
            });
        }
    }
}
