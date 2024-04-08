package projetopi.projetopi.repositorio;

import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.dominio.Barbearia;

import java.util.List;

public interface BarbeariasRepository extends JpaRepository<Barbearia, Integer> {


}