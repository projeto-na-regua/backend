package projetopi.projetopi.dto.request;

public class ParametrosApi {

    private Integer latitude;

    private Integer longitude;

    private String token;

    public ParametrosApi(Integer latitude, Integer longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = System.getenv("TOKEN_API");
    }

    public Integer getLatitude() {
        return latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }


    public String getToken() {
        return token;
    }
}
