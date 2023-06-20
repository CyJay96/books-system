package chief.digital.bookssystem.builder.user;

import chief.digital.bookssystem.builder.TestBuilder;
import chief.digital.bookssystem.model.entity.Library;
import chief.digital.bookssystem.model.entity.User;
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
@NoArgsConstructor(staticName = "aUser")
public class UserTestBuilder implements TestBuilder<User> {

    private Long id = TEST_ID;

    private String username = TEST_STRING;

    private String firstName = TEST_STRING;

    private String lastName = TEST_STRING;

    private String email = TEST_EMAIL;

    private List<Library> libraries = new ArrayList<>();

    @Override
    public User build() {
        return User.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .libraries(libraries)
                .build();
    }
}
