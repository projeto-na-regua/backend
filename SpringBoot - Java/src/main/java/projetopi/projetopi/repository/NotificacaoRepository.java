package projetopi.projetopi.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import projetopi.projetopi.entity.Notificacao;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    List<Notificacao> findByUsuarioNotificadoIdAndTipoNotificacaoIdOrderByDataCriacaoDesc(Integer usuarioId, Integer notificacaoId);


    @Modifying
    @Transactional
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.usuarioNotificado.id = :usuarioId AND n.tipoNotificacao.id = :tipoNotificacaoId AND n.lida = false")
    int marcarNotificacoesComoLidas(Integer usuarioId, Integer tipoNotificacaoId);

    // Método para contar notificações não lidas
    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.usuarioNotificado.id = :usuarioId AND n.tipoNotificacao.id = :tipoNotificacaoId AND n.lida = false")
    Integer contarNotificacoesNaoLidas(Integer usuarioId, Integer tipoNotificacaoId);
}
