package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Mensagem;
import projetopi.projetopi.entity.Usuario;

@Getter
@Setter
public class MensagemResposta {

    private String conteudo;

    private String tipo;

    private String imgPerfil;

    private String midia;

    private String status;

    public MensagemResposta( String status) {
        this.status = status;
    }
}
