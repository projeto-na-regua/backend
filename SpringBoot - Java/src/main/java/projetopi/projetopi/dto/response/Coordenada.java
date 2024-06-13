package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordenada {
    private String lat;
    private String lng;

    public Coordenada(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}