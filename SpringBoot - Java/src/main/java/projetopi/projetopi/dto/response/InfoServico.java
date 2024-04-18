package projetopi.projetopi.dto.response;

import jakarta.persistence.Column;
import projetopi.projetopi.dominio.Servico;

public class InfoServico {

    private Double preco;


    private String descricao;


    private String tipoServico;


    private Integer tempoEstimado;

    private String nomeBarbeiro;

    public InfoServico() {
    }

    public InfoServico(Servico servico) {
        this.preco = servico.getPreco();
        this.descricao = servico.getDescricao();
        this.tipoServico = servico.getTipoServico();
        this.tempoEstimado = servico.getTempoEstimado();
        this.nomeBarbeiro = servico.getBarbeiro().getNome();
    }

    public Double getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public String getNomeBarbeiro() {
        return nomeBarbeiro;
    }
}
