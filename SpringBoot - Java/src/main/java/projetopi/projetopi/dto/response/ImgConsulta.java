package projetopi.projetopi.dto.response;


import lombok.Getter;

@Getter
public class ImgConsulta {

    private String imagem;

    public ImgConsulta(String imagemPerfil) {
        this.imagem = imagemPerfil;
    }
}
