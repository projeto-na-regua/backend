package projetopi.projetopi.dto.response;


import lombok.Getter;

@Getter
public class ImgConsulta {

    private String imagemPerfil;

    public ImgConsulta(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }
}
