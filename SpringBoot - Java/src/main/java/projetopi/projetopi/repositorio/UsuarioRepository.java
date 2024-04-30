package projetopi.projetopi.repositorio;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Usuario;
import projetopi.projetopi.dto.response.InfoUsuario;

import java.util.List;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

List<Usuario> findByNome(String nome);
}
