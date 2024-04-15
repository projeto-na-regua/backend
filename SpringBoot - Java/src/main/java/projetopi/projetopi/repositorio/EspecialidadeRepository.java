package projetopi.projetopi.repositorio;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.dominio.Especialidade;
import projetopi.projetopi.dominio.Servico;

import java.util.List;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Integer> {

}
