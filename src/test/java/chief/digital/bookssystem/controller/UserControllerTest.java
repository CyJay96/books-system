package chief.digital.bookssystem.controller;

import chief.digital.bookssystem.builder.user.UserDtoRequestTestBuilder;
import chief.digital.bookssystem.builder.user.UserDtoResponseTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import chief.digital.bookssystem.service.UserService;
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
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Captor
    ArgumentCaptor<UserDtoRequest> userDtoRequestCaptor;

    private final UserDtoRequest userDtoRequest = UserDtoRequestTestBuilder.aUserDtoRequest().build();
    private final UserDtoResponse expectedUserDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save User")
        void checkSaveShouldReturnUserDtoResponse() {
            doReturn(expectedUserDtoResponse).when(userService).save(userDtoRequest);

            var actualUser = userController.save(userDtoRequest);

            verify(userService).save(any());

            assertAll(
                    () -> assertThat(actualUser.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(actualUser.getBody()).getData())
                            .isEqualTo(expectedUserDtoResponse)
            );
        }

        @Test
        @DisplayName("Save User with Argument Captor")
        void checkSaveByNewsIdWithArgumentCaptorShouldReturnUserDtoResponse() {
            doReturn(expectedUserDtoResponse).when(userService).save(userDtoRequest);

            userController.save(userDtoRequest);

            verify(userService).save(userDtoRequestCaptor.capture());

            assertThat(userDtoRequestCaptor.getValue()).isEqualTo(userDtoRequest);
        }
    }

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserPage() {
        PageResponse<UserDtoResponse> pageResponse = PageResponse.<UserDtoResponse>builder()
                .content(List.of(expectedUserDtoResponse))
                .number(TEST_PAGE)
                .size(TEST_PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(userService).findAll(pageable);

        var actualUsers = userController.findAll(pageable);

        verify(userService).findAll(any());

        assertAll(
                () -> assertThat(actualUsers.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualUsers.getBody()).getData().getContent().stream()
                        .anyMatch(actualUserDtoResponse -> actualUserDtoResponse.equals(expectedUserDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) {
            doReturn(expectedUserDtoResponse).when(userService).findById(id);

            var actualUser = userController.findById(id);

            verify(userService).findById(anyLong());

            assertAll(
                    () -> assertThat(actualUser.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualUser.getBody()).getData())
                            .isEqualTo(expectedUserDtoResponse)
            );
        }

        @Test
        @DisplayName("Find by ID; not found")
        void checkFindByIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userService).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> userController.findById(TEST_ID));

            verify(userService).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnUserDtoResponse(Long id) {
            doReturn(expectedUserDtoResponse).when(userService).update(id, userDtoRequest);

            var actualUser = userController.update(id, userDtoRequest);

            verify(userService).update(anyLong(), any());

            assertAll(
                    () -> assertThat(actualUser.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualUser.getBody()).getData())
                            .isEqualTo(expectedUserDtoResponse)
            );
        }

        @DisplayName("Update User by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnUserDtoResponse(Long id) {
            doReturn(expectedUserDtoResponse).when(userService).update(id, userDtoRequest);

            userController.update(id, userDtoRequest);

            verify(userService).update(anyLong(), userDtoRequestCaptor.capture());

            assertThat(userDtoRequestCaptor.getValue()).isEqualTo(userDtoRequest);
        }

        @Test
        @DisplayName("Update User by ID; not found")
        void checkUpdateShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userService).update(anyLong(), any());

            assertThrows(EntityNotFoundException.class,
                    () -> userController.update(TEST_ID, userDtoRequest)
            );

            verify(userService).update(anyLong(), any());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            doNothing().when(userService).deleteById(id);

            var voidResponse = userController.deleteById(id);

            verify(userService).deleteById(anyLong());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete User by ID; not found")
        void checkDeleteByIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userService).deleteById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> userController.deleteById(TEST_ID));

            verify(userService).deleteById(anyLong());
        }
    }
}
