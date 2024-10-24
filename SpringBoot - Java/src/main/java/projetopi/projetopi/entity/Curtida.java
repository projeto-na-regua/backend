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

    // Getters e Setters
    // ...
}
