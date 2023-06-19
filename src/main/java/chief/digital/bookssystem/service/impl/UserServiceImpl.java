package chief.digital.bookssystem.service.impl;

import chief.digital.bookssystem.exception.EntityNotFoundException;
import chief.digital.bookssystem.mapper.UserMapper;
import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import chief.digital.bookssystem.model.dto.response.PageResponse;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import chief.digital.bookssystem.model.entity.User;
import chief.digital.bookssystem.repository.UserRepository;
import chief.digital.bookssystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDtoResponse save(UserDtoRequest userDtoRequest) {
        User savedUser = userRepository.save(userMapper.toUser(userDtoRequest));
        return userMapper.toUserDtoResponse(savedUser);
    }

    @Override
    public PageResponse<UserDtoResponse> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDtoResponse> userDtoResponses = userPage.stream()
                .map(userMapper::toUserDtoResponse)
                .toList();

        return PageResponse.<UserDtoResponse>builder()
                .content(userDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(userDtoResponses.size())
                .build();
    }

    @Override
    public UserDtoResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    @Override
    public UserDtoResponse update(Long id, UserDtoRequest userDtoRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
        userMapper.updateUser(userDtoRequest, user);
        return userMapper.toUserDtoResponse(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(User.class, id);
        }
        userRepository.deleteById(id);
    }
}
