package projetopi.projetopi.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import projetopi.projetopi.dominio.api.Dado;
import projetopi.projetopi.dominio.api.Precipitacao;
import projetopi.projetopi.dominio.api.Temperatura;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrevisaoApi {

    @JsonDeserialize(converter = Dado.LocalDateTimeDeserializer.class)
    private LocalDateTime dataHora;

    private Double temperatura;

    private Double precipitacao;

    public PrevisaoApi(Temperatura t, Precipitacao p, int index) {
        this.dataHora = t.getTemperatura().get(index).getDataHora();
        this.temperatura = t.getTemperatura().get(index).getValor();
        this.precipitacao = p.getPrecipitacao().get(index).getValor();
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public Double getPrecipitacao() {
        return precipitacao;
    }

    public static class LocalDateTimeDeserializer extends StdConverter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String value) {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
