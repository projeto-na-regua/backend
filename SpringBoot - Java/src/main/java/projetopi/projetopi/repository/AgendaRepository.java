package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projetopi.projetopi.entity.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByClienteIdAndStatus(Integer clienteId, String status);
    List<Agendamento> findByBarbeiroIdAndStatus(Integer BarbeiroId, String status);
    List<Agendamento> findByBarbeariaIdAndStatus(Integer barbeariaId, String status);
    List<Agendamento> findByClienteId(Integer clienteId);
    List<Agendamento> findByBarbeiroId(Integer BarbeiroId);
    List<Agendamento> findByBarbeariaId(Integer barbeariaId);

    @Query("SELECT a FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId AND CAST(a.dataHora AS date) = :date")
    List<Agendamento> findAllByBarbeiroAndDate(@Param("barbeiroId") Integer barbeiroId, @Param("date") LocalDate date);


    @Query("select count(a)>0 from Agendamento a where a.barbeiro.id = ?1 and a.servico.id = ?2 and a.dataHora = ?3 and a.status = 'Agendado'")
    boolean existsByBarbeiroServicoDataHoraConfirmado(Integer barbeiroId, Integer servicoId, LocalDateTime dataHora);
}

