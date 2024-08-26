package projetopi.projetopi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import projetopi.projetopi.entity.Endereco;

import java.time.LocalDateTime;

@Getter
@Setter
public class AgendamentoConsulta {

    private Integer id;

    private LocalDateTime dataHora;

    private String tipoServico;

    private String descricao;

    private Double valorServico;

    private String nomeCliente;

    private String nomeBarbeiro;

    private String nomeNegocio;

    private String imgPerfilBarbearia;

    private String status;

    private EnderecoConsulta enderecoBarbearia;

    private Integer tempoEstimado;

    private LocalDateTime dataHoraPrevista;

    private Double avaliacao;
    private String comentario;


    public AgendamentoConsulta(Integer id, String status, LocalDateTime dataHora, String tipoServico, String descricao, Double valorServico, String nomeCliente, String nomeBarbeiro, String nomeNegocio, Endereco enderecoBarbearia,
       Integer tempoEstimado, Double avaliacao, String comentario) {
        this.dataHora = dataHora;
        this.tipoServico = tipoServico;
        this.descricao = descricao;
        this.valorServico = valorServico;
        this.nomeCliente = nomeCliente;
        this.nomeBarbeiro = nomeBarbeiro;
        this.nomeNegocio = nomeNegocio;
        this.enderecoBarbearia = new EnderecoConsulta(enderecoBarbearia);
        this.status = status;
        this.id = id;
        this.tempoEstimado = tempoEstimado;
        this.dataHoraPrevista = calcularDataHora(dataHora,tempoEstimado);
        this.avaliacao = avaliacao;
        this.comentario = comentario;
    }

    public LocalDateTime calcularDataHora(LocalDateTime dataHora, Integer tempoEstimado) {
        return dataHora.plusMinutes(tempoEstimado);
    }
}
