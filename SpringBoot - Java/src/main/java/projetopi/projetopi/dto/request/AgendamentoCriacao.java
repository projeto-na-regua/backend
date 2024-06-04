package projetopi.projetopi.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AgendamentoCriacao {

    private LocalDateTime dataHora;

    private Integer idServico;

    private Integer idBarbeiro;

    private Integer idCliente;

    private Integer idBarbearia;

}
