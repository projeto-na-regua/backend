package projetopi.projetopi.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "financeiro") // nome da tabela corrigido
public class Financa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_financeiro")
    private Integer id;

    @NotNull
    @Column(name = "valor")
    private Double valor;

    @Column(name = "saldo")
    private Double saldo;

    @Column(name = "despesas")
    private Double despesas;

    @Column(name = "dt_lancamento")
    private LocalDateTime dtLancamento;

    @Column(name = "financeiro_fk_barbearia")
    private Integer barbeariaId;

    public Financa() {
    }

    public Financa(Integer barbeariaId, Double valor) {
        this.barbeariaId = barbeariaId;
        this.valor = valor;
        this.dtLancamento = LocalDateTime.now();
        calcularDespesas();
        this.saldo = calcularSaldoInicial(valor);
    }

    private Double calcularSaldoInicial(Double valor) {
        return valor - despesas;
    }

    private void calcularDespesas() {
        this.despesas = saldo >= 0 ? 0.0 : -saldo;
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

    public Integer getBarbeariaId() {
        return barbeariaId;
    }

    public void setBarbeariaId(Integer barbeariaId) {
        this.barbeariaId = barbeariaId;
    }

    public Double getDespesas() {return despesas;}

    public void setDespesas(Double despesas) {this.despesas = despesas;}

    public Double getSaldo() {return saldo;}

    public void setSaldo(Double saldo) {this.saldo = saldo;}

    public Double getLucro() {
        Double saldo = this.saldo != null ? this.saldo : 0.0;
        Double despesas = this.despesas != null ? this.despesas : 0.0;
        return saldo - despesas;
    }
}
