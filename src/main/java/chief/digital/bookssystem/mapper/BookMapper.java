package chief.digital.bookssystem.mapper;

import chief.digital.bookssystem.model.dto.request.BookDtoRequest;
import chief.digital.bookssystem.model.dto.response.BookDtoResponse;
import chief.digital.bookssystem.model.entity.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "library", ignore = true)
    Book toBook(BookDtoRequest bookDtoRequest);

    @Mapping(
            target = "libraryId",
            expression = "java(java.util.Objects.nonNull(book.getLibrary()) ? book.getLibrary().getId() : null)")
    BookDtoResponse toBookDtoResponse(Book book);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "library", ignore = true)
    void updateBook(BookDtoRequest bookDtoRequest, @MappingTarget Book book);
}
