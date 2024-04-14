package projetopi.projetopi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetopi.projetopi.dominio.Agendamento;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, Integer> {

}

