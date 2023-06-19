package chief.digital.bookssystem.service;

import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface LibraryService {

    LibraryDtoResponse save(LibraryDtoRequest libraryDtoRequest);

    PageResponse<LibraryDtoResponse> findAll(Pageable pageable);

    LibraryDtoResponse findById(Long id);

    LibraryDtoResponse update(Long id, LibraryDtoRequest libraryDtoRequest);

    LibraryDtoResponse addUserByUserId(Long libraryId, Long userId);

    LibraryDtoResponse deleteUserByUserId(Long libraryId, Long userId);

    void deleteById(Long id);
}
