package projetopi.projetopi.repositorio;

import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projetopi.projetopi.dominio.Barbeiro;

import java.util.List;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Integer> {


    List<Barbeiro> findByBarbeariaId(Integer barbeariaId);
}
