package projetopi.projetopi.dto.response.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dado {


    @JsonProperty(value = "dt_txt")
    @JsonDeserialize(converter = Dado.LocalDateTimeDeserializer.class)
    private LocalDateTime data;

    @JsonProperty(value = "pop")
    private Double precipitacao;

    @JsonProperty(value = "main")
    private Temperatura temperatura;

    public LocalDateTime getData() {
        return data;
    }

    public Double getPrecipitacao() {
        return precipitacao;
    }

    public Temperatura getTemperatura() {
        return temperatura;
    }

    public static class LocalDateTimeDeserializer extends StdConverter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String value) {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

}

