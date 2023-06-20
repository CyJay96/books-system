package chief.digital.bookssystem.repository;

import chief.digital.bookssystem.model.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findFirstByOrderByIdAsc();

    Optional<Library> findFirstByOrderByIdDesc();
}
