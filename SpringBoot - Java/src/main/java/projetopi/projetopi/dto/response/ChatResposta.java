package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Chat;
import projetopi.projetopi.entity.Mensagem;

import java.time.LocalDateTime;


@Getter
@Setter
public class ChatResposta {

    private String nomeChat;
    private String ultimaMensage;
    private String imagemPerfil;
    private LocalDateTime dataHoraUltimaMensagem;

    public ChatResposta(Mensagem mensagem, String nomeChat, String imagemPerfil) {
        this.nomeChat = nomeChat;
        this.ultimaMensage = mensagem.getConteudo();
        this.imagemPerfil = imagemPerfil;
        this.dataHoraUltimaMensagem = mensagem.getDataCriacao();
    }
}
