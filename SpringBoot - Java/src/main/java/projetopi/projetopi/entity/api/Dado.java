package projetopi.projetopi.entity.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dado {

    @JsonProperty(value = "date")
    @JsonDeserialize(converter = Dado.LocalDateTimeDeserializer.class)
    private LocalDateTime dataHora;

    @JsonProperty(value = "value")
    private Double valor;

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Double getValor() {
        return valor;
    }

    public static class LocalDateTimeDeserializer extends StdConverter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String value) {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

}

