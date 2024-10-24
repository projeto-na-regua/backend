package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
}
