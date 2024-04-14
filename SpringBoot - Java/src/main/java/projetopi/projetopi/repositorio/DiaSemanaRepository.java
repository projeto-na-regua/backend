package projetopi.projetopi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.dominio.DiaSemana;
import projetopi.projetopi.util.Dia;

import java.util.List;

public interface DiaSemanaRepository extends JpaRepository<DiaSemana, Integer> {

    DiaSemana[] findByBarbeariaId(Integer barbeariaId);
}
