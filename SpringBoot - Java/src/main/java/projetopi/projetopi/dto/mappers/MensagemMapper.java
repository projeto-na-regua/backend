package projetopi.projetopi.dto.mappers;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.stereotype.Component;
import projetopi.projetopi.dto.response.ChatResposta;
import projetopi.projetopi.dto.response.MensagemResposta;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Chat;
import projetopi.projetopi.entity.Mensagem;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.repository.BarbeiroRepository;
import projetopi.projetopi.repository.MensagemRepository;
import projetopi.projetopi.service.ImageService;
import projetopi.projetopi.util.Global;

import java.util.ArrayList;
import java.util.List;


@Component
public class MensagemMapper {

    @Autowired
    private  Global global;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MensagemRepository mensagemRepository;

    public MensagemResposta toDto(Mensagem mensagem, Chat chat){

        MensagemResposta dto = new MensagemResposta(mensagem.getStatusMensagem());
        dto.setConteudo(mensagem.getConteudo());
            if(isBarbearia(mensagem, chat)){
               setImageSeBarbearia(dto, chat);
            }else {
                setImageSeUsuario(dto, chat);
            }

        setMidia(mensagem, dto);
        return dto;
    }

    public List<MensagemResposta> toDtos(List<Mensagem> mensagens, Chat chat){
        List<MensagemResposta> dtos = new ArrayList<>();
        for (Mensagem m : mensagens){
            dtos.add(toDto(m, chat));
        }
        return dtos;
    }

    public List<ChatResposta> toDtos(List<Chat> chats){
        List<ChatResposta> dtos = new ArrayList<>();

        for (Chat chat : chats){
            Mensagem mensagem = mensagemRepository.findFirstByChat_IdOrderByDataCriacaoDesc(chat.getId());
            dtos.add(new ChatResposta(mensagem, getNomeChat(mensagem, chat), getImagePerfil(mensagem, chat)));
        }

        return dtos;
    }


    void setMidia(Mensagem mensagem, MensagemResposta dto){
        if (mensagem.getFilename() != null){
             dto.setMidia(imageService.getImgURL(mensagem.getFilename(), "chat"));
        }
    }

    void setImageSeBarbearia(MensagemResposta dto, Chat chat){
        if (chat.getBarbearia().getImgPerfil() != null){
            dto.setImgPerfil(imageService.getImgURL(chat.getBarbearia().getImgPerfil(), "barbearia"));
        }

        dto.setTipo("barbearia");
    }

    void setImageSeUsuario(MensagemResposta dto, Chat chat){
        if (chat.getUsuario().getImgPerfil() != null){
            dto.setImgPerfil(imageService.getImgURL(chat.getUsuario().getImgPerfil(), "usuario"));
        }
        dto.setTipo("usuario");
    }

    boolean barbeariaUsuarioMesmaDoChat(Mensagem mensagem, Chat chat){
        Barbeiro barbeiro = barbeiroRepository.findById(mensagem.getId()).get();
        return barbeiro.getBarbearia().getId() == chat.getBarbearia().getId();
    }

    String getNomeChat(Mensagem mensagem, Chat chat){
        return isBarbearia(mensagem, chat) ?  chat.getUsuario().getNome() :  chat.getBarbearia().getNomeNegocio();
    }

    String getImagePerfil(Mensagem mensagem, Chat chat){
        String image;
        if (!isBarbearia(mensagem, chat)){
            if (chat.getBarbearia().getImgPerfil() != null){
                return imageService.getImgURL(chat.getBarbearia().getImgPerfil(), "barbearia");
            }
        }else {
            if (chat.getUsuario().getImgPerfil() != null){
                return imageService.getImgURL(chat.getUsuario().getImgPerfil(), "usuario");
            }
        }
        return null;
    }

    boolean isBarbearia(Mensagem mensagem, Chat chat){
        return global.validarSeUsuarioPossuiBarbearia(mensagem.getId()) && barbeariaUsuarioMesmaDoChat(mensagem, chat);
    }
}
