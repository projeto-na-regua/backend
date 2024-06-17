package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Servico;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ServicoConsulta {

    private Integer id;

    private Double preco;

    private String descricao;

    private String tipoServico;

    private Integer tempoEstimado;

    private Boolean status;

    private Set<String> barbeiros;

    public ServicoConsulta() {
    }

    public ServicoConsulta(Servico servico) {
        this.id = servico.getId();
        this.preco = servico.getPreco();
        this.descricao = servico.getDescricao();
        this.tipoServico = servico.getTipoServico();
        this.tempoEstimado = servico.getTempoEstimado();
        this.status = servico.getStatus();
        this.barbeiros = servico.getBarbeiros().stream()
                .map(Barbeiro::getNome)
                .collect(Collectors.toSet());
    }
}
