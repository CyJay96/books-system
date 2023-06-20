package chief.digital.bookssystem.service;

import chief.digital.bookssystem.builder.user.UserDtoRequestTestBuilder;
import chief.digital.bookssystem.builder.user.UserDtoResponseTestBuilder;
import chief.digital.bookssystem.builder.user.UserTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.mapper.UserMapper;
import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import chief.digital.bookssystem.model.entity.User;
import chief.digital.bookssystem.repository.UserRepository;
import chief.digital.bookssystem.service.impl.UserServiceImpl;
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
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Captor
    ArgumentCaptor<User> userCaptor;

    private final UserDtoRequest userDtoRequest = UserDtoRequestTestBuilder.aUserDtoRequest().build();
    private final UserDtoResponse expectedUserDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();
    private final User expectedUser = UserTestBuilder.aUser().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save User")
        void checkSaveShouldReturnUserDtoResponse() {
            doReturn(expectedUser).when(userMapper).toUser(userDtoRequest);
            doReturn(expectedUser).when(userRepository).save(expectedUser);
            doReturn(expectedUserDtoResponse).when(userMapper).toUserDtoResponse(expectedUser);

            UserDtoResponse actualUser = userService.save(userDtoRequest);

            verify(userMapper).toUser(any());
            verify(userRepository).save(any());
            verify(userMapper).toUserDtoResponse(any());

            assertThat(actualUser).isEqualTo(expectedUserDtoResponse);
        }

        @Test
        @DisplayName("Save User with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnUserDtoResponse() {
            doReturn(expectedUser).when(userMapper).toUser(userDtoRequest);
            doReturn(expectedUser).when(userRepository).save(expectedUser);
            doReturn(expectedUserDtoResponse).when(userMapper).toUserDtoResponse(expectedUser);

            userService.save(userDtoRequest);

            verify(userMapper).toUser(any());
            verify(userRepository).save(userCaptor.capture());
            verify(userMapper).toUserDtoResponse(any());

            assertThat(userCaptor.getValue()).isEqualTo(expectedUser);
        }
    }

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedUser))).when(userRepository).findAll(pageable);
        doReturn(expectedUserDtoResponse).when(userMapper).toUserDtoResponse(expectedUser);

        PageResponse<UserDtoResponse> actualUsers = userService.findAll(pageable);

        verify(userRepository).findAll(eq(pageable));
        verify(userMapper).toUserDtoResponse(any());

        assertThat(actualUsers.getContent().stream()
                .anyMatch(actualUserDtoResponse -> actualUserDtoResponse.equals(expectedUserDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) {
            doReturn(Optional.of(expectedUser)).when(userRepository).findById(id);
            doReturn(expectedUserDtoResponse).when(userMapper).toUserDtoResponse(expectedUser);

            UserDtoResponse actualUser = userService.findById(id);

            verify(userRepository).findById(anyLong());
            verify(userMapper).toUserDtoResponse(any());

            assertThat(actualUser).isEqualTo(expectedUserDtoResponse);
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindByIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> userService.findById(TEST_ID));

            verify(userRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnUserDtoResponse(Long id) {
            doReturn(Optional.of(expectedUser)).when(userRepository).findById(id);
            doNothing().when(userMapper).updateUser(userDtoRequest, expectedUser);
            doReturn(expectedUser).when(userRepository).save(expectedUser);
            doReturn(expectedUserDtoResponse).when(userMapper).toUserDtoResponse(expectedUser);

            UserDtoResponse actualUser = userService.update(id, userDtoRequest);

            verify(userRepository).findById(anyLong());
            verify(userMapper).updateUser(any(), any());
            verify(userRepository).save(any());
            verify(userMapper).toUserDtoResponse(any());

            assertThat(actualUser).isEqualTo(expectedUserDtoResponse);
        }

        @DisplayName("Update User by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnUserDtoResponse(Long id) {
            doReturn(Optional.of(expectedUser)).when(userRepository).findById(id);
            doNothing().when(userMapper).updateUser(userDtoRequest, expectedUser);
            doReturn(expectedUser).when(userRepository).save(expectedUser);
            doReturn(expectedUserDtoResponse).when(userMapper).toUserDtoResponse(expectedUser);

            userService.update(id, userDtoRequest);

            verify(userRepository).findById(anyLong());
            verify(userMapper).updateUser(any(), any());
            verify(userRepository).save(userCaptor.capture());
            verify(userMapper).toUserDtoResponse(any());

            assertThat(userCaptor.getValue()).isEqualTo(expectedUser);
        }

        @Test
        @DisplayName("Update User by ID; not found")
        void checkUpdateShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> userService.update(TEST_ID, userDtoRequest)
            );

            verify(userRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnUserDtoResponse(Long id) {
            doReturn(true).when(userRepository).existsById(id);
            doNothing().when(userRepository).deleteById(id);

            userService.deleteById(id);

            verify(userRepository).existsById(anyLong());
            verify(userRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Delete User by ID; not found")
        void checkDeleteByIdShouldThrowUserNotFoundException() {
            doReturn(false).when(userRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> userService.deleteById(TEST_ID));

            verify(userRepository).existsById(anyLong());
        }
    }
}
