package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.Curtida;

import java.util.List;
import java.util.Optional;

public interface CurtidaRepository extends JpaRepository<Curtida, Integer> {
    // Consulta para curtidas ativas em postagem por usuário
    Optional<Curtida> findByUsuario_IdAndPostagem_IdAndIsActiveTrue(Integer usuarioId, Integer postagemId);

    // Consulta para curtidas ativas em comentário por usuário
    Optional<Curtida> findByUsuario_IdAndComentario_IdAndIsActiveTrue(Integer usuarioId, Integer comentarioId);

    // Lista todas as curtidas ativas em uma postagem
    List<Curtida> findByPostagem_IdAndIsActiveTrue(Integer postagemId);

    // Lista todas as curtidas ativas em um comentário
    List<Curtida> findByComentario_IdAndIsActiveTrue(Integer comentarioId);

    // Conta o total de curtidas ativas em uma postagem
    int countByPostagem_IdAndIsActiveTrue(Integer postagemId);

    // Conta o total de curtidas ativas em um comentário
    int countByComentario_IdAndIsActiveTrue(Integer comentarioId);

    // Consulta para curtidas ativas em postagem por usuário
    Optional<Curtida> findByUsuario_IdAndPostagem_Id(Integer usuarioId, Integer postagemId);

    // Consulta para curtidas ativas em comentário por usuário
    Optional<Curtida> findByUsuario_IdAndComentario_Id(Integer usuarioId, Integer comentarioId);

}
