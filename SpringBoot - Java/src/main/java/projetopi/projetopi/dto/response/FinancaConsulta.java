package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FinancaConsulta {

    private Double despesa;
    private Double lucro;
    private Double saldo;
    private Double lucratividade;
    private TotalServicoPorDia servico;

    public FinancaConsulta(Double despesa, Double lucro, Double saldo) {
        this.saldo = saldo;
        this.lucro = lucro;
        this.despesa = despesa;
    }
}
