package projetopi.projetopi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.dominio.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
