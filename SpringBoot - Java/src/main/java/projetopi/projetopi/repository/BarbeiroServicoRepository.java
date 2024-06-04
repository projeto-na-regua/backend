package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.BarbeiroServico;
import projetopi.projetopi.entity.BarbeiroServicoId;

public interface BarbeiroServicoRepository extends JpaRepository<BarbeiroServico, BarbeiroServicoId> {
}