package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.DiaSemana;
import projetopi.projetopi.util.Dia;

public interface DiaSemanaRepository extends JpaRepository<DiaSemana, Integer> {

    DiaSemana[] findByBarbeariaId(Integer barbeariaId);

    DiaSemana findByNomeAndBarbeariaId(Dia s, int i);
}
