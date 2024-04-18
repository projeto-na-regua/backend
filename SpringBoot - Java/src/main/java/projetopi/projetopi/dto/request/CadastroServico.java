package projetopi.projetopi.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.web.bind.annotation.GetMapping;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Servico;

public class CadastroServico {


    private Double preco;

    private String descricao;

    private String tipoServico;

    private Integer tempoEstimado;

    private Integer fkBarbeiro;

    public Servico gerarServico(){
        Servico servico = new Servico();

        servico.setPreco(preco);
        servico.setDescricao(descricao);
        servico.setTempoEstimado(tempoEstimado);
        servico.setTipoServico(tipoServico);

        return servico;
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

    public Integer getFkBarbeiro() {
        return fkBarbeiro;
    }
}
