package projetopi.projetopi.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Precipitacao {

    @JsonProperty(value = "precipitations")
    private List<Dado> precipitacao;

    public List<Dado> getPrecipitacao() {
        return precipitacao;
    }
}
