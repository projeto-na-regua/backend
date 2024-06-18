package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class AvaliacaoConsulta {

    private String nomeCliente;
    private String nomeBarbeiro;
    private Double resultadoAvaliacao;
    private String comentario;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    public AvaliacaoConsulta(LocalDate date, String nomeBarbeiro, String nomeCliente, Double resultadoAvaliacao, String comentario) {
        this.nomeBarbeiro = nomeBarbeiro;
        this.data = date;
        this.nomeCliente = nomeCliente;
        this.resultadoAvaliacao = resultadoAvaliacao;
        this.comentario = comentario;
    }
}
