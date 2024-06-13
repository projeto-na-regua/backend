package projetopi.projetopi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HorarioDiaSemana {

    private String hora; // hh:mm
    private Integer idBarbeiro;
    private Integer idServico;

}
