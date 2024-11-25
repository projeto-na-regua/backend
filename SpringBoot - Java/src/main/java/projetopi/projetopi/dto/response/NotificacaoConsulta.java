package projetopi.projetopi.dto.response;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projetopi.projetopi.entity.Notificacao;
import projetopi.projetopi.entity.TipoNotificacao;
import projetopi.projetopi.entity.Usuario;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificacaoConsulta {

    private Integer id;
    private String titulo;
    private String tipoNotificacao;
    private Integer referenciaId;
    private String descricao;
    private LocalDateTime dataCriacao;
    private boolean lida;

    public NotificacaoConsulta(Notificacao notificacao, String titulo, Integer referenciaId) {
        this.id = notificacao.getId();
        this.tipoNotificacao = notificacao.getTipoNotificacao().getNome();
        this.referenciaId = id;
        this.descricao = notificacao.getMensagem();
        this.dataCriacao = notificacao.getDataCriacao();
        this.lida = notificacao.isLida();
    }

    public NotificacaoConsulta(Notificacao notificacao, String titulo) {
        this.id = notificacao.getId();
        this.tipoNotificacao = notificacao.getTipoNotificacao().getNome();
        this.referenciaId = notificacao.getReferenciaId();
        this.descricao = notificacao.getMensagem();
        this.dataCriacao = notificacao.getDataCriacao();
        this.lida = notificacao.isLida();
    }
}
