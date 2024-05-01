package projetopi.projetopi.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "financeiro") // nome da tabela corrigido
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
    @JoinColumn(name = "financeiro_fk_barberia") // nome da coluna de chave estrangeira corrigido
    private Barbearia barbearia;

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
