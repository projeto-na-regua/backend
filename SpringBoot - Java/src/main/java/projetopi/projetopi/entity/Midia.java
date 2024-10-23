package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "midia")
@Getter
@Setter
@NoArgsConstructor
public class Midia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_midia")
    private Integer id;

    @Column(name = "arquivo", nullable = false, columnDefinition = "TEXT")
    private String arquivo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midia_id_postagem", nullable = true)
    private Postagem postagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midia_id_comentario", nullable = true)
    private Comentario comentario;

}
