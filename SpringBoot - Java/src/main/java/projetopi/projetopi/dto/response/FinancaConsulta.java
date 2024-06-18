package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class FinancaConsulta {

    private Double despesa;
    private Double lucro;
    private Double receita;
    private Double lucratividade;
    private String[][] servicos;

    public FinancaConsulta(Double despesa, Double receita, Double lucro) {
        this.receita = receita;
        this.lucro = lucro;
        this.despesa = despesa;
    }

    public void definiMatriz(Integer qtd){
        servicos = new String[2][qtd];
    }
}
