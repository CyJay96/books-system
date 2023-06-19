package chief.digital.bookssystem.controller;

import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.model.dto.response.APIResponse;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static chief.digital.bookssystem.controller.LibraryController.LIBRARY_API_PATH;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = LIBRARY_API_PATH)
@Tag(name = "LibraryController", description = "Library API")
public class LibraryController {

    public static final String LIBRARY_API_PATH = "/api/v0/libraries";

    private final LibraryService libraryService;

    @Operation(summary = "Save Library", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saved Library")
    })
    @PostMapping
    public ResponseEntity<APIResponse<LibraryDtoResponse>> save(@RequestBody @Valid LibraryDtoRequest libraryDtoRequest) {
        LibraryDtoResponse library = libraryService.save(libraryDtoRequest);

        return APIResponse.of(
                "Library with ID " + library.getId() + " was created",
                LIBRARY_API_PATH,
                HttpStatus.CREATED,
                library
        );
    }

    @Operation(summary = "Find all Libraries", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Libraries")
    })
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<LibraryDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<LibraryDtoResponse> libraries = libraryService.findAll(pageable);

        return APIResponse.of(
                "All Libraries: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                LIBRARY_API_PATH,
                HttpStatus.OK,
                libraries
        );
    }

    @Operation(summary = "Find Library by ID", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Library by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<LibraryDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        LibraryDtoResponse library = libraryService.findById(id);

        return APIResponse.of(
                "Library with ID " + library.getId() + " was found",
                LIBRARY_API_PATH + "/" + id,
                HttpStatus.OK,
                library
        );
    }

    @Operation(summary = "Update Library by ID", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Library by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<LibraryDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid LibraryDtoRequest libraryDtoRequest) {
        LibraryDtoResponse library = libraryService.update(id, libraryDtoRequest);

        return APIResponse.of(
                "Changes were applied to the Library with ID " + id,
                LIBRARY_API_PATH + "/" + id,
                HttpStatus.OK,
                library
        );
    }

    @Operation(summary = "Partial Update Library by ID", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partial Updated Library by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<APIResponse<LibraryDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody LibraryDtoRequest libraryDtoRequest) {
        LibraryDtoResponse library = libraryService.update(id, libraryDtoRequest);

        return APIResponse.of(
                "Partial changes were applied to the Library with ID " + id,
                LIBRARY_API_PATH + "/" + id,
                HttpStatus.OK,
                library
        );
    }

    @Operation(summary = "Add User to Library by User ID", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added User to Library by User ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/addUser/{libraryId}/{userId}")
    public ResponseEntity<APIResponse<LibraryDtoResponse>> addUserByUserId(
            @PathVariable @NotNull @PositiveOrZero Long libraryId,
            @PathVariable @NotNull @PositiveOrZero Long userId) {
        LibraryDtoResponse library = libraryService.addUserByUserId(libraryId, userId);

        return APIResponse.of(
                "User with ID " + userId + " was added to the Library with ID " + libraryId,
                LIBRARY_API_PATH + "/" + libraryId + "/" + userId,
                HttpStatus.OK,
                library
        );
    }

    @Operation(summary = "Delete User to Library by User ID", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted User to Library by User ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/deleteUser/{libraryId}/{userId}")
    public ResponseEntity<APIResponse<LibraryDtoResponse>> deleteUserByUserId(
            @PathVariable @NotNull @PositiveOrZero Long libraryId,
            @PathVariable @NotNull @PositiveOrZero Long userId) {
        LibraryDtoResponse library = libraryService.deleteUserByUserId(libraryId, userId);

        return APIResponse.of(
                "User with ID " + userId + " was deleted to the Library with ID " + libraryId,
                LIBRARY_API_PATH + "/" + libraryId + "/" + userId,
                HttpStatus.OK,
                library
        );
    }

    @Operation(summary = "Delete Library by ID", tags = "LibraryController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted Library by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        libraryService.deleteById(id);

        return APIResponse.of(
                "Library with ID " + id + " was deleted",
                LIBRARY_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
