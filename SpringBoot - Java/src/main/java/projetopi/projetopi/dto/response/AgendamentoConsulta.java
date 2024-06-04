package projetopi.projetopi.dto.response;

import lombok.Getter;
import org.springframework.cglib.core.Local;
import projetopi.projetopi.entity.Endereco;

import java.time.LocalDateTime;

@Getter
public class AgendamentoConsulta {

    private LocalDateTime dataHora;

    private String tipoServico;

    private String descricao;

    private Double valorServico;

    private String nomeCliente;

    private String nomeBarbeiro;

    private String nomeNegocio;

    private Endereco enderecoBarbearia;

    public AgendamentoConsulta(LocalDateTime dataHora, String tipoServico, String descricao, Double valorServico, String nomeCliente, String nomeBarbeiro, String nomeNegocio, Endereco enderecoBarbearia) {
        this.dataHora = dataHora;
        this.tipoServico = tipoServico;
        this.descricao = descricao;
        this.valorServico = valorServico;
        this.nomeCliente = nomeCliente;
        this.nomeBarbeiro = nomeBarbeiro;
        this.nomeNegocio = nomeNegocio;
        this.enderecoBarbearia = enderecoBarbearia;
    }
}
