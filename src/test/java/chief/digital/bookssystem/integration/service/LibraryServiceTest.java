package chief.digital.bookssystem.integration.service;

import chief.digital.bookssystem.builder.library.LibraryDtoRequestTestBuilder;
import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.integration.BaseIntegrationTest;
import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.repository.LibraryRepository;
import chief.digital.bookssystem.repository.UserRepository;
import chief.digital.bookssystem.service.LibraryService;
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
class LibraryServiceTest extends BaseIntegrationTest {

    private final LibraryService libraryService;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    private final LibraryDtoRequest libraryDtoRequest = LibraryDtoRequestTestBuilder.aLibraryDtoRequest().build();
    private final Pageable pageable = PageRequest.of(TEST_PAGE, TEST_PAGE_SIZE);

    @Test
    @DisplayName("Save Library")
    void checkSaveShouldReturnLibraryDtoResponse() {
        LibraryDtoResponse actualLibrary = libraryService.save(libraryDtoRequest);
        assertThat(actualLibrary.getTitle()).isEqualTo(libraryDtoRequest.getTitle());
    }

    @Test
    @DisplayName("Find all Library")
    void checkFindAllShouldReturnLibraryDtoResponsePage() {
        int expectedLibrarySize = (int) libraryRepository.count();
        PageResponse<LibraryDtoResponse> actualLibrary = libraryService.findAll(pageable);
        assertThat(actualLibrary.getContent()).hasSize(expectedLibrarySize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkFindByIdShouldReturnLibraryDtoResponse(Long id) {
            LibraryDtoResponse actualLibrary = libraryService.findById(id);
            assertThat(actualLibrary.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find Library by ID; not found")
        void checkFindByIdShouldThrowLibraryNotFoundException() {
            Long doesntExistLibraryId = new Random()
                    .nextLong(libraryRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> libraryService.findById(doesntExistLibraryId));
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkUpdateShouldReturnLibraryDtoResponse(Long id) {
            LibraryDtoResponse actualLibrary = libraryService.update(id, libraryDtoRequest);
            assertThat(actualLibrary.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update Library by ID; not found")
        void checkUpdateShouldThrowLibraryNotFoundException() {
            Long doesntExistLibraryId = new Random()
                    .nextLong(libraryRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> libraryService.update(doesntExistLibraryId, libraryDtoRequest)
            );
        }
    }

    @Test
    @DisplayName("Add User to Library by User ID")
    void checkAddUserByUserIdShouldReturnLibraryDtoResponse() {
        Long existsLibraryId = libraryRepository.findFirstByOrderByIdAsc().get().getId();
        Long existsUserId = userRepository.findFirstByOrderByIdDesc().get().getId();
        LibraryDtoResponse actualLibrary = libraryService.addUserByUserId(existsLibraryId, existsUserId);
        assertThat(actualLibrary.getUsersIds().contains(existsUserId)).isTrue();
    }

    @Test
    @DisplayName("Delete User to Library by User ID")
    void checkDeleteUserByUserIdShouldReturnLibraryDtoResponse() {
        Long existsLibraryId = libraryRepository.findFirstByOrderByIdAsc().get().getId();
        Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
        LibraryDtoResponse actualLibrary = libraryService.deleteUserByUserId(existsLibraryId, existsUserId);
        assertThat(actualLibrary.getUsersIds().contains(existsUserId)).isFalse();
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Library by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            libraryService.deleteById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete Library by ID; not found")
        void checkDeleteByIdShouldThrowLibraryNotFoundException() {
            Long doesntExistLibraryId = new Random()
                    .nextLong(libraryRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> {
                libraryService.deleteById(doesntExistLibraryId);
                entityManager.flush();
            });
        }
    }
}
