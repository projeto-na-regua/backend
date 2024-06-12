package projetopi.projetopi.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetopi.projetopi.entity.Agendamento;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByClienteIdAndStatus(Integer clienteId, String status);
    List<Agendamento> findByBarbeiroIdAndStatus(Integer BarbeiroId, String status);
    List<Agendamento> findByBarbeariaIdAndStatus(Integer barbeariaId, String status);
    List<Agendamento> findByClienteId(Integer clienteId);
    List<Agendamento> findByBarbeiroId(Integer BarbeiroId);
    List<Agendamento> findByBarbeariaId(Integer barbeariaId);
}

