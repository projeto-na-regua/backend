package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class BarbeariaServicoPesquisa {
    private BarbeariaPesquisa barbearia;
    private List<ServicoConsulta> servicos;
    private LocalDate date;


}
