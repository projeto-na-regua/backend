package projetopi.projetopi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.dominio.Cliente;
import projetopi.projetopi.dto.response.InfoUsuario;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("select new Cliente(c.nome, c.email, c.celular) from Cliente c where c.id = :id")
    List<InfoUsuario> findByInfoUsuario(@Param("id") Integer id);
}
