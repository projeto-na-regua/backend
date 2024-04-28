package projetopi.projetopi.dto.response.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Previsao {



   @JsonProperty(value = "list")
   private List<Dado> dados;

    public List<Dado> getDados() {
        return dados;
    }
}
