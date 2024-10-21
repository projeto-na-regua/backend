package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="avaliacao")
@Getter
@Setter
public class Avaliacao {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_avaliacao", nullable = false)
    private Integer id;
    @Column(name="resultado_avaliacao", columnDefinition = "decimal(3,2)")
    private Double resultadoAvaliacao;
    @Column(name="comentario")
    private String comentario;

}

