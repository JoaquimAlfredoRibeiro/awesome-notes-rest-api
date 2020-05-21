package pt.home.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.home.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
