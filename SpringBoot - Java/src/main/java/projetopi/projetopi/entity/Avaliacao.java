package projetopi.projetopi.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="avaliacao")
public class Avaliacao {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_avaliacao", nullable = false)
    private Integer id;
    @Column(name="resultado_avaliacao", columnDefinition = "DECIMAL(3,2)")
    private BigDecimal resultadoAvaliacao;

    public Avaliacao(Integer id, BigDecimal resultadoAvaliacao) {
        this.id = id;
        this.resultadoAvaliacao = resultadoAvaliacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public BigDecimal getResultadoAvaliacao() {
        return resultadoAvaliacao;
    }
    public void setResultadoAvaliacao(BigDecimal resultadoAvaliacao) {
        this.resultadoAvaliacao = resultadoAvaliacao;
    }
}
