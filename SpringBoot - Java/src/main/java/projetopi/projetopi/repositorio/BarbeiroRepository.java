package projetopi.projetopi.repositorio;

import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dto.response.InfoUsuario;

import java.util.List;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Integer> {


    List<Barbeiro> findByBarbeariaId(Integer barbeariaId);

    @Query("select new Barbeiro(b.nome, b.email, b.celular) from Barbeiro b where b.id = :id")
    List<InfoUsuario> findByInfoUsuario(@Param("id") Integer id);

}
