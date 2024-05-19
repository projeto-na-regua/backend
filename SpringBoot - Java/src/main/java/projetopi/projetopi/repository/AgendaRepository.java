package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetopi.projetopi.entity.Agendamento;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, Integer> {

}

