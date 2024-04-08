package projetopi.projetopi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.dominio.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
}
