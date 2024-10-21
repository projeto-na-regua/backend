package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import projetopi.projetopi.dto.request.MensagemCriacao;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_mensagem", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mensagem_id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "mensagem_id_chat")
    private Chat chat;

    @JoinColumn(name = "conteudo")
    private String conteudo;

    @JoinColumn(name = "status_mensagem")
    private String statusMensagem;

    @JoinColumn(name = "tipo")
    private String tipo;

    @Column(name="filename")
    private String filename;

    @Column(name="data_criacao")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    public Mensagem() {}

    public Mensagem(Chat chat, Usuario usuario, String mensagem) {
        this.chat = chat;
        this.usuario = usuario;
        this.conteudo = mensagem;
        this.statusMensagem = "Enviada";
        this.dataCriacao = LocalDateTime.now();
    }
}
