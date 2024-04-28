package projetopi.projetopi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.dominio.Financa;

import java.util.List;
import java.util.Optional;

public interface FinanceiroRepository extends JpaRepository<Financa, Integer> {
    Optional<Financa> findById(Integer Id);

    List<Financa> findByBarbeariaId(Integer barbeariaId);


}
