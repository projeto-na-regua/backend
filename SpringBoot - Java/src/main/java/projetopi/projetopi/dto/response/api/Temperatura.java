package projetopi.projetopi.dto.response.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperatura {


    @JsonProperty(value = "temp_min")
    private Integer min;

    @JsonProperty(value = "temp_max")
    private Integer max;

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
