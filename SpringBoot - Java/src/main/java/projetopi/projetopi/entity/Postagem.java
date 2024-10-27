package projetopi.projetopi.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import projetopi.projetopi.dto.response.PostConsulta;

import java.time.LocalDateTime;

@Entity
@Table(name = "postagem")
@Getter
@Setter
@NoArgsConstructor
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postagem")
    private Integer id;

    @Column(name = "conteudo", nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_criacao", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "is_active")
    private Boolean isActive;

    public Postagem(String conteudo, Usuario usuario) {
        this.conteudo = conteudo;
        this.usuario = usuario;
        this.isActive = true;
        this.dataCriacao = LocalDateTime.now();
    }
}