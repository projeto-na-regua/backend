package projetopi.projetopi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
public class TotalServicoPorDia {

    Double total;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDate data;

    public TotalServicoPorDia(Double total, LocalDate datas) {
        this.total = total;
        this.data = datas;
    }



}
