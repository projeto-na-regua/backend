package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
@Setter
public class TotalValorPorDia {
    private Long total;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    private Double precos;

    public TotalValorPorDia(LocalDate data, Long total) {
        this.data = data;
        this.total = total;
    }

    public TotalValorPorDia(Double preco, LocalDate data) {
        this.data = data;
        this.precos = preco;
    }
}
