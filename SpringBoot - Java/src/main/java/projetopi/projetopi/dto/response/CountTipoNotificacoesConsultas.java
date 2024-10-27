package projetopi.projetopi.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountTipoNotificacoesConsultas {

    private Integer totalNotMensagens;
    private Integer totalNotAgenadamentos;
    private Integer totalNotComunidade;

    public CountTipoNotificacoesConsultas(Integer totalNotMensagens, Integer totalNotAgenadamentos, Integer totalNotComunidade) {
        this.totalNotMensagens = totalNotMensagens;
        this.totalNotAgenadamentos = totalNotAgenadamentos;
        this.totalNotComunidade = totalNotComunidade;
    }
}
