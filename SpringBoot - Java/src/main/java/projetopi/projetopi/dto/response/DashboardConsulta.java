package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class DashboardConsulta {

    private Long confirmados;

    private Long pendentes;

    private Long cancelados;

    public DashboardConsulta(Long confirmados, Long pendentes, Long cancelados) {
        this.confirmados = confirmados;
        this.pendentes = pendentes;
        this.cancelados = cancelados;
    }
}
