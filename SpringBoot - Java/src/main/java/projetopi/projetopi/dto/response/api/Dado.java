package projetopi.projetopi.dto.response.api;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Dado {


    @JsonProperty(value = "dt_txt")
    private LocalDate data;


    @JsonProperty(value = "rain")
    private Chuva chuva;

    @JsonProperty(value = "temperature")
    private Temperatura temperatura;

    public LocalDate getData() {
        return data;
    }

    public Chuva getChuva() {
        return chuva;
    }

    public Temperatura getTemperatura() {
        return temperatura;
    }
}
