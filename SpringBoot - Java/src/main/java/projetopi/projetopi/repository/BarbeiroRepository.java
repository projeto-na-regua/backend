package projetopi.projetopi.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.dto.response.UsuarioConsulta;

import java.util.List;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Integer> {


    List<Barbeiro> findByBarbeariaId(Integer barbeariaId);

    @Query("SELECT b FROM Barbeiro b WHERE b.barbearia.id = ?1 AND b.id <> ?2")
    List<Barbeiro> findByBarbeariaIdAndUsuarioIdNot(Integer barbeariaId, Integer usuarioId);

    @Query("select new Barbeiro(b.nome, b.email, b.celular) from Barbeiro b where b.id = :id")
    List<UsuarioConsulta> findByInfoUsuario(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.dtype = 'Cliente', u.barbearia = null,  u.adm = false WHERE u.id = :barbeiroId")
    void atualizarBarbeiroParaCliente(@Param("barbeiroId") Integer barbeiroId);

    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.barbearia = :novaBarbearia, u.adm = :novoAdm WHERE u.id = :barbeiroId")
    void atualizarBarbeariaEAdm(
            @Param("barbeiroId") Integer barbeiroId,
            @Param("novaBarbearia") Barbearia novaBarbearia,
            @Param("novoAdm") boolean novoAdm
    );


    Barbeiro findByEmail(String email);




}
