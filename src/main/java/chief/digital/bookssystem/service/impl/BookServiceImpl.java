package chief.digital.bookssystem.service.impl;

import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.mapper.BookMapper;
import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.entity.Book;
import chief.digital.bookssystem.model.entity.Library;
import chief.digital.bookssystem.repository.BookRepository;
import chief.digital.bookssystem.repository.LibraryRepository;
import chief.digital.bookssystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDtoResponse saveByLibraryId(Long libraryId, BookDtoRequest bookDtoRequest) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException(Library.class, libraryId));

        Book book = bookMapper.toBook(bookDtoRequest);
        book.setLibrary(library);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDtoResponse(savedBook);
    }

    @Override
    public PageResponse<BookDtoResponse> findAll(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);

        List<BookDtoResponse> bookDtoResponses = bookPage.stream()
                .map(bookMapper::toBookDtoResponse)
                .toList();

        return PageResponse.<BookDtoResponse>builder()
                .content(bookDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(bookDtoResponses.size())
                .build();
    }

    @Override
    public BookDtoResponse findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(Book.class, id));
    }

    @Override
    public BookDtoResponse update(Long id, BookDtoRequest bookDtoRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Book.class, id));
        bookMapper.updateBook(bookDtoRequest, book);
        return bookMapper.toBookDtoResponse(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(Book.class, id);
        }
        bookRepository.deleteById(id);
    }
}
