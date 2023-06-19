package chief.digital.bookssystem.repository;

import chief.digital.bookssystem.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
