package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.Chat;

import java.util.List;
import java.util.Optional;


public interface ChatRepository extends JpaRepository<Chat, Integer> {


    Optional<Chat> findByUsuario_IdAndBarbearia_Id(Integer usuarioId, Integer barbeariaId);

    List<Chat> findByUsuario_Id(Integer usuarioId);
    List<Chat> findByBarbearia_Id(Integer barbeariaId);

}
