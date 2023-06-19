package chief.digital.bookssystem.repository;

import chief.digital.bookssystem.model.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
