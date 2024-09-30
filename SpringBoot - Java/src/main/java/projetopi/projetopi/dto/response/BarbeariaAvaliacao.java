package projetopi.projetopi.dto.response;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class BarbeariaAvaliacao {

    private String nomeBarbearia;
    private Double mediaAvaliacoes;
    private String imgPerfilBarbearia;



    public BarbeariaAvaliacao(String nomeBarbearia, Double mediaAvaliacoes, String imgPerfilBarbearia) {
        this.nomeBarbearia = nomeBarbearia;
        this.mediaAvaliacoes = mediaAvaliacoes;
        this.imgPerfilBarbearia = imgPerfilBarbearia;
    }
}
