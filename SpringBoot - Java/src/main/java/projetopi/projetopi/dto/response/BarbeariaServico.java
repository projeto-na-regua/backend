package projetopi.projetopi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Servico;

@Getter
public class BarbeariaServico {
    private BarbeariaPesquisa barbearia;
    private ServicoConsulta servico;

    public BarbeariaServico(Barbearia barbearia, Servico servico) {
        this.barbearia = new BarbeariaPesquisa(barbearia, 0.);
        this.servico = new ServicoConsulta(servico);
    }
}
