package projetopi.projetopi.dto.response;

import lombok.Getter;
import projetopi.projetopi.entity.Servico;

@Getter
public class ServicoConsulta {

    private Integer id;

    private Double preco;

    private String descricao;

    private String tipoServico;

    private Integer tempoEstimado;

    private String nomeBarbeiro;

    public ServicoConsulta() {
    }

    public ServicoConsulta(Servico servico) {
        this.id = servico.getId();
        this.preco = servico.getPreco();
        this.descricao = servico.getDescricao();
        this.tipoServico = servico.getTipoServico();
        this.tempoEstimado = servico.getTempoEstimado();
        this.nomeBarbeiro = servico.getBarbeiro().getNome();
    }

}
