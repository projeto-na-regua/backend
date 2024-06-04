package projetopi.projetopi.dto.request;

import lombok.Getter;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Servico;

import java.util.List;


@Getter
public class ServicoCriacao {

    private Double preco;

    private String descricao;

    private String tipoServico;

    private Integer tempoEstimado;

    private List<Integer> barbeirosIds;

}


