package chief.digital.bookssystem.mapper;

import chief.digital.bookssystem.model.dto.request.LibraryDtoRequest;
import chief.digital.bookssystem.model.dto.response.LibraryDtoResponse;
import chief.digital.bookssystem.model.entity.Library;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface LibraryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "users", ignore = true)
    Library toLibrary(LibraryDtoRequest libraryDtoRequest);

    @Mapping(
            target = "booksIds",
            expression = "java(java.util.Objects.nonNull(library.getBooks()) ? " +
                    "library.getBooks().stream().map(book -> book.getId()).toList() : null)")
    @Mapping(
            target = "usersIds",
            expression = "java(java.util.Objects.nonNull(library.getUsers()) ? " +
                    "library.getUsers().stream().map(user -> user.getId()).toList() : null)")
    LibraryDtoResponse toLibraryDtoResponse(Library library);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateLibrary(LibraryDtoRequest libraryDtoRequest, @MappingTarget Library library);
}
