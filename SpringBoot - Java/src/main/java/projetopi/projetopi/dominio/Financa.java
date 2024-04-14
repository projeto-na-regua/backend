package projetopi.projetopi.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Financa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_financeiro")
    private Integer id;
    @Column(name="valor")
    private Double valor;
    @Column(name="dt_lancamento")
    private LocalDateTime dtLancamento;

    @OneToOne
    @PrimaryKeyJoinColumn(name="financeiro_fk_barberia")
    private Barbearia barbearia;

    public Financa(Integer id, Double valor, LocalDateTime dtLancamento, Barbearia barbearia) {
        this.id = id;
        this.valor = valor;
        this.dtLancamento = dtLancamento;
        this.barbearia = barbearia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(LocalDateTime dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    public Barbearia getBarbearia() {
        return barbearia;
    }

    public void setBarbearia(Barbearia barbearia) {
        this.barbearia = barbearia;
    }
}
