package chief.digital.bookssystem.repository;

import chief.digital.bookssystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
