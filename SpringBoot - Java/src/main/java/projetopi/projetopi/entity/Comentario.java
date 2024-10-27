package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentario")
@Getter
@Setter
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Integer id;

    @Column(name = "conteudo", nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    // Mapeamento da chave estrangeira para a entidade Postagem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id_postagem", nullable = false)
    private Postagem postagem;

    // Mapeamento da chave estrangeira para a entidade Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "is_active")
    private Boolean isActive;

    public Comentario(String conteudo, Postagem postagem, Usuario usuario) {
        this.conteudo = conteudo;
        this.dataCriacao = LocalDateTime.now();
        this.postagem = postagem;
        this.usuario = usuario;
        this.isActive = true;
    }
}
