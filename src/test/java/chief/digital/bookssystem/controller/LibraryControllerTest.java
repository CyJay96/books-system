package chief.digital.bookssystem.controller;

import chief.digital.bookssystem.builder.library.LibraryDtoRequestTestBuilder;
import chief.digital.bookssystem.builder.library.LibraryDtoResponseTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.service.LibraryService;
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
public class LibraryControllerTest {

    @InjectMocks
    private LibraryController libraryController;

    @Mock
    private LibraryService libraryService;

    @Captor
    ArgumentCaptor<LibraryDtoRequest> libraryDtoRequestCaptor;

    private final LibraryDtoRequest libraryDtoRequest = LibraryDtoRequestTestBuilder.aLibraryDtoRequest().build();
    private final LibraryDtoResponse expectedLibraryDtoResponse = LibraryDtoResponseTestBuilder.aLibraryDtoResponse().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @BeforeEach
    void setUp() {
        libraryController = new LibraryController(libraryService);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Library")
        void checkSaveShouldReturnLibraryDtoResponse() {
            doReturn(expectedLibraryDtoResponse).when(libraryService).save(libraryDtoRequest);

            var actualLibrary = libraryController.save(libraryDtoRequest);

            verify(libraryService).save(any());

            assertAll(
                    () -> assertThat(actualLibrary.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(actualLibrary.getBody()).getData())
                            .isEqualTo(expectedLibraryDtoResponse)
            );
        }

        @Test
        @DisplayName("Save Library with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnLibraryDtoResponse() {
            doReturn(expectedLibraryDtoResponse).when(libraryService).save(libraryDtoRequest);

            libraryController.save(libraryDtoRequest);

            verify(libraryService).save(libraryDtoRequestCaptor.capture());

            assertThat(libraryDtoRequestCaptor.getValue()).isEqualTo(libraryDtoRequest);
        }
    }

    @Test
    @DisplayName("Find all Library")
    void checkFindAllShouldReturnLibraryPage() {
        PageResponse<LibraryDtoResponse> pageResponse = PageResponse.<LibraryDtoResponse>builder()
                .content(List.of(expectedLibraryDtoResponse))
                .number(TEST_PAGE)
                .size(TEST_PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(libraryService).findAll(pageable);

        var actualLibrary = libraryController.findAll(pageable);

        verify(libraryService).findAll(any());

        assertAll(
                () -> assertThat(actualLibrary.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualLibrary.getBody()).getData().getContent().stream()
                        .anyMatch(actualLibraryDtoResponse -> actualLibraryDtoResponse.equals(expectedLibraryDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnLibraryDtoResponse(Long id) {
            doReturn(expectedLibraryDtoResponse).when(libraryService).findById(id);

            var actualLibrary = libraryController.findById(id);

            verify(libraryService).findById(anyLong());

            assertAll(
                    () -> assertThat(actualLibrary.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualLibrary.getBody()).getData())
                            .isEqualTo(expectedLibraryDtoResponse)
            );
        }

        @Test
        @DisplayName("Find by ID; not found")
        void checkFindByIdShouldThrowLibraryNotFoundException() {
            doThrow(EntityNotFoundException.class).when(libraryService).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> libraryController.findById(TEST_ID));

            verify(libraryService).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnLibraryDtoResponse(Long id) {
            doReturn(expectedLibraryDtoResponse).when(libraryService).update(id, libraryDtoRequest);

            var actualLibrary = libraryController.update(id, libraryDtoRequest);

            verify(libraryService).update(anyLong(), any());

            assertAll(
                    () -> assertThat(actualLibrary.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualLibrary.getBody()).getData())
                            .isEqualTo(expectedLibraryDtoResponse)
            );
        }

        @DisplayName("Update Library by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnLibraryDtoResponse(Long id) {
            doReturn(expectedLibraryDtoResponse).when(libraryService).update(id, libraryDtoRequest);

            libraryController.update(id, libraryDtoRequest);

            verify(libraryService).update(anyLong(), libraryDtoRequestCaptor.capture());

            assertThat(libraryDtoRequestCaptor.getValue()).isEqualTo(libraryDtoRequest);
        }

        @Test
        @DisplayName("Update Library by ID; not found")
        void checkUpdateShouldThrowLibraryNotFoundException() {
            doThrow(EntityNotFoundException.class).when(libraryService).update(anyLong(), any());

            assertThrows(EntityNotFoundException.class,
                    () -> libraryController.update(TEST_ID, libraryDtoRequest)
            );

            verify(libraryService).update(anyLong(), any());
        }
    }

    @Test
    @DisplayName("Add User to Library by User ID")
    void checkAddUserByUserIdShouldReturnLibraryDtoResponse() {
        doReturn(expectedLibraryDtoResponse).when(libraryService).addUserByUserId(TEST_ID, TEST_ID);

        var actualLibrary = libraryController.addUserByUserId(TEST_ID, TEST_ID);

        verify(libraryService).addUserByUserId(anyLong(), any());

        assertAll(
                () -> assertThat(actualLibrary.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualLibrary.getBody()).getData())
                        .isEqualTo(expectedLibraryDtoResponse)
        );
    }

    @Test
    @DisplayName("Delete User to Library by User ID")
    void checkDeleteUserByUserIdShouldReturnLibraryDtoResponse() {
        doReturn(expectedLibraryDtoResponse).when(libraryService).deleteUserByUserId(TEST_ID, TEST_ID);

        var actualLibrary = libraryController.deleteUserByUserId(TEST_ID, TEST_ID);

        verify(libraryService).deleteUserByUserId(anyLong(), any());

        assertAll(
                () -> assertThat(actualLibrary.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualLibrary.getBody()).getData())
                        .isEqualTo(expectedLibraryDtoResponse)
        );
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            doNothing().when(libraryService).deleteById(id);

            var voidResponse = libraryController.deleteById(id);

            verify(libraryService).deleteById(anyLong());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete Library by ID; not found")
        void checkDeleteByIdShouldThrowLibraryNotFoundException() {
            doThrow(EntityNotFoundException.class).when(libraryService).deleteById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> libraryController.deleteById(TEST_ID));

            verify(libraryService).deleteById(anyLong());
        }
    }
}
