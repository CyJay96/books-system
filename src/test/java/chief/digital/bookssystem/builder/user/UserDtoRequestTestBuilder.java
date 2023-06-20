package chief.digital.bookssystem.builder.user;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.dto.request.UserDtoRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import static chief.digital.bookssystem.util.TestConstants.TEST_EMAIL;
import static chief.digital.bookssystem.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserDtoRequest")
public class UserDtoRequestTestBuilder implements TestBuilder<UserDtoRequest> {

    private String username = TEST_STRING;

    private String firstName = TEST_STRING;

    private String lastName = TEST_STRING;

    private String email = TEST_EMAIL;

    @Override
    public UserDtoRequest build() {
        return UserDtoRequest.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }
}
