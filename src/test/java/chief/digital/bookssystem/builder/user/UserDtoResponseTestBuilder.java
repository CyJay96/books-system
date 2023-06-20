package chief.digital.bookssystem.builder.user;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.dto.response.UserDtoResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

import static chief.digital.bookssystem.util.TestConstants.TEST_EMAIL;
import static chief.digital.bookssystem.util.TestConstants.TEST_ID;
import static chief.digital.bookssystem.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserDtoResponse")
public class UserDtoResponseTestBuilder implements TestBuilder<UserDtoResponse> {

    private Long id = TEST_ID;

    private String username = TEST_STRING;

    private String firstName = TEST_STRING;

    private String lastName = TEST_STRING;

    private String email = TEST_EMAIL;

    private List<Long> librariesIds = new ArrayList<>();

    @Override
    public UserDtoResponse build() {
        return UserDtoResponse.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .librariesIds(librariesIds)
                .build();
    }
}
