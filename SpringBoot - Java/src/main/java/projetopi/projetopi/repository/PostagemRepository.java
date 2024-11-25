package projetopi.projetopi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.Postagem;

import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Integer> {

    /// Busca as últimas 10 postagens ativas
    @Query(value = "SELECT p FROM Postagem p WHERE p.isActive = true ORDER BY p.dataCriacao DESC")
    List<Postagem> findTop10ByOrderByDataCriacaoDesc();

    // Busca todas as postagens ativas de um usuário específico
    @Query("SELECT p FROM Postagem p WHERE p.usuario.id = :usuarioId AND p.isActive = true")
    List<Postagem> findAllByUsuario_IdAndIsActiveTrue(@Param("usuarioId") Integer usuarioId);


}
