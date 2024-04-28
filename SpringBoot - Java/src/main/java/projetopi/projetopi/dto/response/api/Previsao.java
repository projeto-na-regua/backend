package projetopi.projetopi.dto.response.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class Previsao {



   @JsonProperty(value = "list")
   private Dado data;

    public Dado getData() {
        return data;
    }
}
