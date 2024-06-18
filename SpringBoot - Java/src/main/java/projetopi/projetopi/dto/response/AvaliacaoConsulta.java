package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AvaliacaoConsulta {

    private String nomeCliente;
    private Double resultadoAvaliacao;
    private String comentario;

    public AvaliacaoConsulta(String nomeCliente, Double resultadoAvaliacao, String comentario) {
        this.nomeCliente = nomeCliente;
        this.resultadoAvaliacao = resultadoAvaliacao;
        this.comentario = comentario;
    }
}
