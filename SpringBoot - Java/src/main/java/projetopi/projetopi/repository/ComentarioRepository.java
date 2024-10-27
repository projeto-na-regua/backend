package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.Comentario;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    // Traz todos os comentários ativos de uma postagem específica
    @Query("SELECT c FROM Comentario c WHERE c.postagem.id = :postagemId AND c.isActive = true")
    List<Comentario> findAllActiveByPostagemId(@Param("postagemId") Integer postagemId);

    // Conta todos os comentários ativos de uma postagem específica
    @Query("SELECT COUNT(c) FROM Comentario c WHERE c.postagem.id = :postagemId AND c.isActive = true")
    Integer countActiveByPostagem_Id(@Param("postagemId") Integer postagemId);


}
