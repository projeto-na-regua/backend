package projetopi.projetopi.dto.request;

import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Servico;

import java.util.List;


@Getter
@Setter
public class ServicoCriacao {

    private Double preco;

    private String descricao;

    private String tipoServico;

    private Integer tempoEstimado;

    private List<String> barbeirosEmails;

}


