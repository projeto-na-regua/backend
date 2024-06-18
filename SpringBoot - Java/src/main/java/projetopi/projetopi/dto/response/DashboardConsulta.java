package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class DashboardConsulta {

    private Long confirmados;

    private Long pendentes;

    private Long cancelados;

    private Double mediaAvaliacoes;

    private List<LocalDate> datasGrafico;

    private List<Long> valoresGrafico;

    public DashboardConsulta(Long pendentes, Long cancelados, Long confirmados) {
        this.confirmados = confirmados;
        this.pendentes = pendentes;
        this.cancelados = cancelados;
    }
}
