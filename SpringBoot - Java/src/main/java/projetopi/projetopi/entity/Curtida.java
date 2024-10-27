package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "curtida")
@Getter
@Setter
@NoArgsConstructor

public class Curtida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curtida")
    private Integer id;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    // Relacionamento com Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curtida_id_usuario", nullable = false)
    private Usuario usuario;

    // Relacionamento opcional com Postagem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curtida_id_postagem")
    private Postagem postagem;

    // Relacionamento opcional com Comentario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curtida_id_comentario")
    private Comentario comentario;

    @Column(name = "is_active")
    private Boolean isActive;

    public Curtida(Usuario usuario, Postagem postagem) {
        this.dataCriacao = LocalDateTime.now();
        this.usuario = usuario;
        this.postagem = postagem;
        this.isActive = true;
    }

    public Curtida(Usuario usuario, Comentario comentario) {
        this.dataCriacao = LocalDateTime.now();
        this.usuario = usuario;
        this.comentario = comentario;
        this.isActive = true;
    }
}
