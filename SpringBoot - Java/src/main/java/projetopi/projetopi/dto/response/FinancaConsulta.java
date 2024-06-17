package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class FinancaConsulta {

    private Double despesa;
    private Double lucro;
    private Double saldo;
    private Double lucratividade;
    private List<Double> servicosPreco;
    private List<LocalDate> servicosData;

    public FinancaConsulta(Double despesa, Double lucro, Double saldo) {
        this.saldo = saldo;
        this.lucro = lucro;
        this.despesa = despesa;
    }
}
