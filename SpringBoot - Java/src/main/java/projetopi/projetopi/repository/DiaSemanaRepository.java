package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.DiaSemana;

public interface DiaSemanaRepository extends JpaRepository<DiaSemana, Integer> {

    DiaSemana[] findByBarbeariaId(Integer barbeariaId);
}
