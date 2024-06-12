package projetopi.projetopi.dto.response;

import lombok.Getter;
import org.springframework.cglib.core.Local;
import projetopi.projetopi.entity.Endereco;

import java.time.LocalDateTime;

@Getter
public class AgendamentoConsulta {

    private Integer id;

    private LocalDateTime dataHora;

    private String tipoServico;

    private String descricao;

    private Double valorServico;

    private String nomeCliente;

    private String nomeBarbeiro;

    private String nomeNegocio;

    private String status;

    private Endereco enderecoBarbearia;

    public AgendamentoConsulta(Integer id, String status, LocalDateTime dataHora, String tipoServico, String descricao, Double valorServico, String nomeCliente, String nomeBarbeiro, String nomeNegocio, Endereco enderecoBarbearia) {
        this.dataHora = dataHora;
        this.tipoServico = tipoServico;
        this.descricao = descricao;
        this.valorServico = valorServico;
        this.nomeCliente = nomeCliente;
        this.nomeBarbeiro = nomeBarbeiro;
        this.nomeNegocio = nomeNegocio;
        this.enderecoBarbearia = enderecoBarbearia;
        this.status = status;
        this.id = id;
    }
}
