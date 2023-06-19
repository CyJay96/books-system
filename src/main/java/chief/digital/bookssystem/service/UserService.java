package chief.digital.bookssystem.service;

import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDtoResponse save(UserDtoRequest userDtoRequest);

    PageResponse<UserDtoResponse> findAll(Pageable pageable);

    UserDtoResponse findById(Long id);

    UserDtoResponse update(Long id, UserDtoRequest userDtoRequest);

    void deleteById(Long id);
}
