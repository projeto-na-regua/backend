package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.TipoNotificacao;

public interface TipoNotificacaoRepository extends JpaRepository<TipoNotificacao, Integer> {

    TipoNotificacao findByNome(String nome);

    boolean existsByNome(String nome);
}
