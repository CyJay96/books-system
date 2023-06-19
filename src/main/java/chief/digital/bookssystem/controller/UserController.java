package chief.digital.bookssystem.controller;

import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import chief.digital.bookssystem.model.dto.response.APIResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import chief.digital.bookssystem.service.UserService;
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

import static chief.digital.bookssystem.controller.UserController.USER_API_PATH;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = USER_API_PATH)
@Tag(name = "UserController", description = "User API")
public class UserController {

    public static final String USER_API_PATH = "/api/v0/users";

    private final UserService userService;

    @Operation(summary = "Save User", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saved User")
    })
    @PostMapping
    public ResponseEntity<APIResponse<UserDtoResponse>> save(@RequestBody @Valid UserDtoRequest userDtoRequest) {
        UserDtoResponse user = userService.save(userDtoRequest);

        return APIResponse.of(
                "User with ID " + user.getId() + " was created",
                USER_API_PATH,
                HttpStatus.CREATED,
                user
        );
    }

    @Operation(summary = "Find all Users", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Users")
    })
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<UserDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<UserDtoResponse> users = userService.findAll(pageable);

        return APIResponse.of(
                "All Users: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                USER_API_PATH,
                HttpStatus.OK,
                users
        );
    }

    @Operation(summary = "Find User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        UserDtoResponse user = userService.findById(id);

        return APIResponse.of(
                "User with ID " + user.getId() + " was found",
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    @Operation(summary = "Update User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid UserDtoRequest userDtoRequest) {
        UserDtoResponse user = userService.update(id, userDtoRequest);

        return APIResponse.of(
                "Changes were applied to the User with ID " + id,
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    @Operation(summary = "Partial Update User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partial Updated User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<APIResponse<UserDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse user = userService.update(id, userDtoRequest);

        return APIResponse.of(
                "Partial changes were applied to the User with ID " + id,
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                user
        );
    }

    @Operation(summary = "Delete User by ID", tags = "UserController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted User by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        userService.deleteById(id);

        return APIResponse.of(
                "User with ID " + id + " was deleted",
                USER_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
