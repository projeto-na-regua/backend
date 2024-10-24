package projetopi.projetopi.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.Notificacao;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
}
