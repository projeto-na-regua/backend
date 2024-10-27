package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midia_id_postagem", nullable = true)
    private Postagem postagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midia_id_comentario", nullable = true)
    private Comentario comentario;

    public Midia(String arquivo, Postagem postagem) {
        this.arquivo = arquivo;
        this.postagem = postagem;
        this.dataCriacao = LocalDateTime.now();
    }

    public Midia(String arquivo, Comentario comentario) {
        this.arquivo = arquivo;
        this.comentario = comentario;
        this.dataCriacao = LocalDateTime.now();
    }


}
