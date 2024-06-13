package projetopi.projetopi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
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


    private Integer tempoEstimado;

    private LocalDateTime dataHoraPrevista;


    public AgendamentoConsulta(Integer id, String status, LocalDateTime dataHora, String tipoServico, String descricao, Double valorServico, String nomeCliente, String nomeBarbeiro, String nomeNegocio, Endereco enderecoBarbearia,
       Integer tempoEstimado) {
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
        this.tempoEstimado = tempoEstimado;
        this.dataHoraPrevista = calcularDataHora(dataHora,tempoEstimado);
    }

    public LocalDateTime calcularDataHora(LocalDateTime dataHora, Integer tempoEstimado) {
        return dataHora.plusMinutes(tempoEstimado);
    }
}
