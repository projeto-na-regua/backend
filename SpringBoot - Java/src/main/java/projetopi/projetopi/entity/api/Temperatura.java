package projetopi.projetopi.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Temperatura {

    @JsonProperty(value = "temperatures")
    private List<Dado> temperatura;

    public List<Dado> getTemperatura() {
        return temperatura;
    }
}
