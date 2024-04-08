package projetopi.projetopi.repositorio;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Usuario;

import java.util.List;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}
