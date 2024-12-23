package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacao")
    private Integer id;

    // Relacionamento com Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notificacao_id_usuario", nullable = false)
    private Usuario usuario;

    // Relacionamento com Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notificacao_id_usuario_notificado", nullable = false)
    private Usuario usuarioNotificado;

    // Relacionamento com TipoNotificacao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notificacao_id_tipo", nullable = false)
    private TipoNotificacao tipoNotificacao;

    @Column(name = "referencia_id", nullable = true)
    private Integer referenciaId;

    @Column(name = "mensagem", columnDefinition = "TEXT", nullable = true)
    private String mensagem;

    @Column(name = "data_criacao", nullable = true)
    private LocalDateTime dataCriacao;

    @Column(name = "lida", nullable = false, columnDefinition = "BIT(1)")
    private boolean lida;

    public Notificacao(Usuario usuario, TipoNotificacao tipoNotificacao, Integer referenciaId, String mensagem, Usuario usuarioNotificado) {
        this.usuario = usuario;
        this.tipoNotificacao = tipoNotificacao;
        this.referenciaId = referenciaId;
        this.mensagem = mensagem;
        this.dataCriacao = LocalDateTime.now();
        this.lida = false;
        this.usuarioNotificado = usuarioNotificado;
    }
}