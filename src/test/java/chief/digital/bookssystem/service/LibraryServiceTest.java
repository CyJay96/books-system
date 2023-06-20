package chief.digital.bookssystem.service;

import chief.digital.bookssystem.builder.library.LibraryDtoRequestTestBuilder;
import chief.digital.bookssystem.builder.library.LibraryDtoResponseTestBuilder;
import chief.digital.bookssystem.builder.library.LibraryTestBuilder;
import chief.digital.bookssystem.builder.user.UserTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.mapper.LibraryMapper;
import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.entity.Library;
import chief.digital.bookssystem.model.entity.User;
import chief.digital.bookssystem.repository.LibraryRepository;
import chief.digital.bookssystem.repository.UserRepository;
import chief.digital.bookssystem.service.impl.LibraryServiceImpl;
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
class LibraryServiceTest {

    private LibraryService libraryService;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LibraryMapper libraryMapper;

    @Captor
    ArgumentCaptor<Library> libraryCaptor;

    private final LibraryDtoRequest libraryDtoRequest = LibraryDtoRequestTestBuilder.aLibraryDtoRequest().build();
    private final LibraryDtoResponse expectedLibraryDtoResponse = LibraryDtoResponseTestBuilder.aLibraryDtoResponse().build();
    private final Library expectedLibrary = LibraryTestBuilder.aLibrary().build();
    private final User expectedUser = UserTestBuilder.aUser().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        libraryService = new LibraryServiceImpl(libraryRepository, userRepository, libraryMapper);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Library")
        void checkSaveShouldReturnLibraryDtoResponse() {
            doReturn(expectedLibrary).when(libraryMapper).toLibrary(libraryDtoRequest);
            doReturn(expectedLibrary).when(libraryRepository).save(expectedLibrary);
            doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

            LibraryDtoResponse actualLibrary = libraryService.save(libraryDtoRequest);

            verify(libraryMapper).toLibrary(any());
            verify(libraryRepository).save(any());
            verify(libraryMapper).toLibraryDtoResponse(any());

            assertThat(actualLibrary).isEqualTo(expectedLibraryDtoResponse);
        }

        @Test
        @DisplayName("Save Library with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnLibraryDtoResponse() {
            doReturn(expectedLibrary).when(libraryMapper).toLibrary(libraryDtoRequest);
            doReturn(expectedLibrary).when(libraryRepository).save(expectedLibrary);
            doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

            libraryService.save(libraryDtoRequest);

            verify(libraryMapper).toLibrary(any());
            verify(libraryRepository).save(libraryCaptor.capture());
            verify(libraryMapper).toLibraryDtoResponse(any());

            assertThat(libraryCaptor.getValue()).isEqualTo(expectedLibrary);
        }
    }

    @Test
    @DisplayName("Find all Library")
    void checkFindAllShouldReturnLibraryDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedLibrary))).when(libraryRepository).findAll(pageable);
        doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

        PageResponse<LibraryDtoResponse> actualLibrary = libraryService.findAll(pageable);

        verify(libraryRepository).findAll(eq(pageable));
        verify(libraryMapper).toLibraryDtoResponse(any());

        assertThat(actualLibrary.getContent().stream()
                .anyMatch(actualLibraryDtoResponse -> actualLibraryDtoResponse.equals(expectedLibraryDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnLibraryDtoResponse(Long id) {
            doReturn(Optional.of(expectedLibrary)).when(libraryRepository).findById(id);
            doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

            LibraryDtoResponse actualLibrary = libraryService.findById(id);

            verify(libraryRepository).findById(anyLong());
            verify(libraryMapper).toLibraryDtoResponse(any());

            assertThat(actualLibrary).isEqualTo(expectedLibraryDtoResponse);
        }

        @Test
        @DisplayName("Find Library by ID; not found")
        void checkFindByIdShouldThrowLibraryNotFoundException() {
            doThrow(EntityNotFoundException.class).when(libraryRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> libraryService.findById(TEST_ID));

            verify(libraryRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnLibraryDtoResponse(Long id) {
            doReturn(Optional.of(expectedLibrary)).when(libraryRepository).findById(id);
            doNothing().when(libraryMapper).updateLibrary(libraryDtoRequest, expectedLibrary);
            doReturn(expectedLibrary).when(libraryRepository).save(expectedLibrary);
            doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

            LibraryDtoResponse actualLibrary = libraryService.update(id, libraryDtoRequest);

            verify(libraryRepository).findById(anyLong());
            verify(libraryMapper).updateLibrary(any(), any());
            verify(libraryRepository).save(any());
            verify(libraryMapper).toLibraryDtoResponse(any());

            assertThat(actualLibrary).isEqualTo(expectedLibraryDtoResponse);
        }

        @DisplayName("Update Library by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnLibraryDtoResponse(Long id) {
            doReturn(Optional.of(expectedLibrary)).when(libraryRepository).findById(id);
            doNothing().when(libraryMapper).updateLibrary(libraryDtoRequest, expectedLibrary);
            doReturn(expectedLibrary).when(libraryRepository).save(expectedLibrary);
            doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

            libraryService.update(id, libraryDtoRequest);

            verify(libraryRepository).findById(anyLong());
            verify(libraryMapper).updateLibrary(any(), any());
            verify(libraryRepository).save(libraryCaptor.capture());
            verify(libraryMapper).toLibraryDtoResponse(any());

            assertThat(libraryCaptor.getValue()).isEqualTo(expectedLibrary);
        }

        @Test
        @DisplayName("Update Library by ID; not found")
        void checkUpdateShouldThrowLibraryNotFoundException() {
            doThrow(EntityNotFoundException.class).when(libraryRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> libraryService.update(TEST_ID, libraryDtoRequest));

            verify(libraryRepository).findById(anyLong());
        }
    }

    @Test
    @DisplayName("Add User to Library by User ID")
    void checkAddUserByUserIdShouldReturnLibraryDtoResponse() {
        doReturn(Optional.of(expectedLibrary)).when(libraryRepository).findById(TEST_ID);
        doReturn(Optional.of(expectedUser)).when(userRepository).findById(TEST_ID);
        doReturn(expectedLibrary).when(libraryRepository).save(expectedLibrary);
        doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

        LibraryDtoResponse actualLibrary = libraryService.addUserByUserId(TEST_ID, TEST_ID);

        verify(libraryRepository).findById(anyLong());
        verify(userRepository).findById(anyLong());
        verify(libraryRepository).save(any());
        verify(libraryMapper).toLibraryDtoResponse(any());

        assertThat(actualLibrary).isEqualTo(expectedLibraryDtoResponse);
    }

    @Test
    @DisplayName("Delete User from Library by User ID")
    void checkDeleteUserByUserIdShouldReturnLibraryDtoResponse() {
        doReturn(Optional.of(expectedLibrary)).when(libraryRepository).findById(TEST_ID);
        doReturn(Optional.of(expectedUser)).when(userRepository).findById(TEST_ID);
        doReturn(expectedLibrary).when(libraryRepository).save(expectedLibrary);
        doReturn(expectedLibraryDtoResponse).when(libraryMapper).toLibraryDtoResponse(expectedLibrary);

        LibraryDtoResponse actualLibrary = libraryService.deleteUserByUserId(TEST_ID, TEST_ID);

        verify(libraryRepository).findById(anyLong());
        verify(userRepository).findById(anyLong());
        verify(libraryRepository).save(any());
        verify(libraryMapper).toLibraryDtoResponse(any());

        assertThat(actualLibrary).isEqualTo(expectedLibraryDtoResponse);
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnLibraryDtoResponse(Long id) {
            doReturn(true).when(libraryRepository).existsById(id);
            doNothing().when(libraryRepository).deleteById(id);

            libraryService.deleteById(id);

            verify(libraryRepository).existsById(anyLong());
            verify(libraryRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Delete Library by ID; not found")
        void checkDeleteByIdShouldThrowLibraryNotFoundException() {
            doReturn(false).when(libraryRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> libraryService.deleteById(TEST_ID));

            verify(libraryRepository).existsById(anyLong());
        }
    }
}
