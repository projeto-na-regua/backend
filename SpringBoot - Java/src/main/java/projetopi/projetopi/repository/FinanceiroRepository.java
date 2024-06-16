package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.dto.response.FinancaConsulta;
import projetopi.projetopi.entity.Financa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FinanceiroRepository extends JpaRepository<Financa, Integer> {
    Optional<Financa> findById(Integer Id);

    List<Financa> findByBarbeariaId(Integer barbeariaId);



    @Query("SELECT new projetopi.projetopi.dto.response.FinancaConsulta( " +
            "    SUM(CASE WHEN f.despesas = true THEN f.valor ELSE 0 END), " +
            "    SUM(CASE WHEN f.despesas = false THEN f.valor ELSE 0 END), " +
            "    SUM(CASE WHEN f.despesas = false THEN f.valor ELSE 0 END) - " +
            "    SUM(CASE WHEN f.despesas = true THEN f.valor ELSE 0 END)) " +
            "FROM Financa f " +
            "WHERE f.barbearia.id = :id " +
            "AND CONVERT(date, f.dtLancamento) BETWEEN :dataInicial AND :dataFinal")
    FinancaConsulta findByFinancasByBarbeariaIdAndBetweenDates(
            @Param("id") Integer id,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal);


}
