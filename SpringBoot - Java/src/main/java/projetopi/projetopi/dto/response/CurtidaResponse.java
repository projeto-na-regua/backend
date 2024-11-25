package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Curtida;

import java.time.LocalDateTime;



@Getter
@Setter
public class CurtidaResponse {

    private LocalDateTime dataCriacao;
    private String quemCurtiu;
    private String userNameQuemCutiu;
    private Integer id;

    public CurtidaResponse(Curtida curtida) {
        this.dataCriacao = curtida.getDataCriacao();
        this.quemCurtiu = curtida.getUsuario().getNome();
        this.userNameQuemCutiu = curtida.getUsuario().getUsername();
        this.id = curtida.getId();
    }

    public CurtidaResponse() {
    }
}
