package chief.digital.bookssystem.mapper;

import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import chief.digital.bookssystem.model.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "libraries", ignore = true)
    User toUser(UserDtoRequest userDtoRequest);

    @Mapping(
            target = "librariesIds",
            expression = "java(java.util.Objects.nonNull(user.getLibraries()) ? " +
                    "user.getLibraries().stream().map(library -> library.getId()).toList() : null)")
    UserDtoResponse toUserDtoResponse(User user);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "libraries", ignore = true)
    void updateUser(UserDtoRequest userDtoRequest, @MappingTarget User user);
}
