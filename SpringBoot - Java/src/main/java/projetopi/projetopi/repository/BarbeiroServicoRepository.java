package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.*;

public interface BarbeiroServicoRepository extends JpaRepository<BarbeiroServico, BarbeiroServicoId> {

    @Query("SELECT bs FROM BarbeiroServico bs WHERE bs.barbeiro.id = :barbeiroId AND bs.servico.id = :servicoId AND bs.barbearia.id = :barbeariaId")
    BarbeiroServico findByBarbeiroAndServicoAndBarbearia(@Param("barbeiroId") Integer barbeiroId, @Param("servicoId") Integer servicoId, @Param("barbeariaId") Integer barbeariaId);
}