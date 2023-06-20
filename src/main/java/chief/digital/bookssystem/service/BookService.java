package chief.digital.bookssystem.service;

import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDtoResponse saveByLibraryId(Long libraryId, BookDtoRequest bookDtoRequest);

    PageResponse<BookDtoResponse> findAll(Pageable pageable);

    BookDtoResponse findById(Long id);

    BookDtoResponse update(Long id, BookDtoRequest bookDtoRequest);

    void deleteById(Long id);
}
