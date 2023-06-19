package chief.digital.bookssystem.service.impl;

import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.mapper.LibraryMapper;
import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.entity.Library;
import chief.digital.bookssystem.repository.LibraryRepository;
import chief.digital.bookssystem.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;

    @Override
    public LibraryDtoResponse save(LibraryDtoRequest libraryDtoRequest) {
        Library library = libraryMapper.toLibrary(libraryDtoRequest);
        Library savedLibrary = libraryRepository.save(library);
        return libraryMapper.toLibraryDtoResponse(savedLibrary);
    }

    @Override
    public PageResponse<LibraryDtoResponse> findAll(Pageable pageable) {
        Page<Library> libraryPage = libraryRepository.findAll(pageable);

        List<LibraryDtoResponse> libraryDtoResponses = libraryPage.stream()
                .map(libraryMapper::toLibraryDtoResponse)
                .toList();

        return PageResponse.<LibraryDtoResponse>builder()
                .content(libraryDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(libraryDtoResponses.size())
                .build();
    }

    @Override
    public LibraryDtoResponse findById(Long id) {
        return libraryRepository.findById(id)
                .map(libraryMapper::toLibraryDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(Library.class, id));
    }

    @Override
    public LibraryDtoResponse update(Long id, LibraryDtoRequest libraryDtoRequest) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Library.class, id));
        libraryMapper.updateLibrary(libraryDtoRequest, library);
        return libraryMapper.toLibraryDtoResponse(libraryRepository.save(library));
    }

    @Override
    public void deleteById(Long id) {
        if (!libraryRepository.existsById(id)) {
            throw new EntityNotFoundException(Library.class, id);
        }
        libraryRepository.deleteById(id);
    }
}
