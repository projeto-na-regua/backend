package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "financeiro")
public class Financa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_financeiro")
    private Integer id;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "dt_lancamento")
    private LocalDateTime dtLancamento;

    @ManyToOne
    @JoinColumn(name = "financeiro_fk_barbearia")
    private Barbearia barbearia;

    @JoinColumn(name = "despesas")
    private Boolean despesas;

    public Financa() {
    }

    public Financa(Integer id, Double valor, LocalDateTime dtLancamento, Barbearia barbearia) {
        this.id = id;
        this.valor = valor;
        this.dtLancamento = dtLancamento;
        this.barbearia = barbearia;
    }

    public Financa(Barbearia barbearia, LocalDateTime dtLancamento, Double valor) {
        this.barbearia = barbearia;
        this.dtLancamento = dtLancamento;
        this.valor = valor;
    }

}
