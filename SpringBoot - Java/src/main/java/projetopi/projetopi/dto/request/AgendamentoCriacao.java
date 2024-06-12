package projetopi.projetopi.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AgendamentoCriacao {

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;

    private Integer idServico;

    private Integer idBarbeiro;

    private Integer idBarbearia;

}
