package chief.digital.bookssystem.controller;

import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.model.dto.response.APIResponse;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.service.BookService;
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

import static chief.digital.bookssystem.controller.BookController.USER_API_PATH;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = USER_API_PATH)
@Tag(name = "BookController", description = "Book API")
public class BookController {

    public static final String USER_API_PATH = "/api/v0/books";

    private final BookService bookService;

    @Operation(summary = "Save Book", tags = "BookController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saved Book")
    })
    @PostMapping("/{libraryId}")
    public ResponseEntity<APIResponse<BookDtoResponse>> saveByLibraryId(
            @PathVariable @NotNull @PositiveOrZero Long libraryId,
            @RequestBody @Valid BookDtoRequest bookDtoRequest) {
        BookDtoResponse book = bookService.saveByLibraryId(libraryId, bookDtoRequest);

        return APIResponse.of(
                "Book with ID " + book.getId() + " was created",
                USER_API_PATH,
                HttpStatus.CREATED,
                book
        );
    }

    @Operation(summary = "Find all Books", tags = "BookController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Books")
    })
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<BookDtoResponse>>> findAll(Pageable pageable) {
        PageResponse<BookDtoResponse> books = bookService.findAll(pageable);

        return APIResponse.of(
                "All Books: page_number: " + pageable.getPageNumber() +
                        "; page_size: " + pageable.getPageSize(),
                USER_API_PATH,
                HttpStatus.OK,
                books
        );
    }

    @Operation(summary = "Find Book by ID", tags = "BookController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Book by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<BookDtoResponse>> findById(@PathVariable @NotNull @PositiveOrZero Long id) {
        BookDtoResponse book = bookService.findById(id);

        return APIResponse.of(
                "Book with ID " + book.getId() + " was found",
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                book
        );
    }

    @Operation(summary = "Update Book by ID", tags = "BookController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Book by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<BookDtoResponse>> update(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody @Valid BookDtoRequest bookDtoRequest) {
        BookDtoResponse book = bookService.update(id, bookDtoRequest);

        return APIResponse.of(
                "Changes were applied to the Book with ID " + id,
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                book
        );
    }

    @Operation(summary = "Partial Update Book by ID", tags = "BookController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partial Updated Book by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<APIResponse<BookDtoResponse>> updatePartially(
            @PathVariable @NotNull @PositiveOrZero Long id,
            @RequestBody BookDtoRequest bookDtoRequest) {
        BookDtoResponse book = bookService.update(id, bookDtoRequest);

        return APIResponse.of(
                "Partial changes were applied to the Book with ID " + id,
                USER_API_PATH + "/" + id,
                HttpStatus.OK,
                book
        );
    }

    @Operation(summary = "Delete Book by ID", tags = "BookController")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted Book by ID"),
            @ApiResponse(responseCode = "404", description = "Entity not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteById(@PathVariable @NotNull @PositiveOrZero Long id) {
        bookService.deleteById(id);

        return APIResponse.of(
                "Book with ID " + id + " was deleted",
                USER_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                null
        );
    }
}
