package projetopi.projetopi.dominio;

import jakarta.persistence.*;

@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_servico")
    private Integer id;
    @Column(name="preco")
    private Double preco;
    @Column(name="descricao")
    private String descricao;
    @Column(name="tipo_servico")
    private String tipoServico;
    @Column(name="tempo_estimado", nullable = false)
    private Integer tempoEstimado;
    @ManyToOne
    @JoinColumn(name="servico_fk_barbeiro", nullable = false)
    private Barbeiro barbeiro;
    @ManyToOne
    @JoinColumn(name="servico_fk_barbearia")
    private Barbearia barbearia;

    private String nomeBarbeiro;

    public Servico(Integer id, Double preco, String descricao, String tipoServico, Integer tempoEstimado, Barbeiro barbeiro, Barbearia barbearia) {
        this.id = id;
        this.preco = preco;
        this.descricao = descricao;
        this.tipoServico = tipoServico;
        this.tempoEstimado = tempoEstimado;
        this.barbeiro = barbeiro;
        this.barbearia = barbearia;
    }

    public Servico(Double preco, String descricao, String tipoServico, Integer tempoEstimado, String nomebarbeiro) {
        this.preco = preco;
        this.descricao = descricao;
        this.tipoServico = tipoServico;
        this.tempoEstimado = tempoEstimado;
        this.barbeiro = new Barbeiro(nomebarbeiro);
    }

    public Servico() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public Barbeiro getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Barbeiro barbeiro) {
        this.barbeiro = barbeiro;
    }


    public Barbearia getBarbearia() {
        return barbearia;
    }

    public void setBarbearia(Barbearia barbearia) {
        this.barbearia = barbearia;
    }

}
