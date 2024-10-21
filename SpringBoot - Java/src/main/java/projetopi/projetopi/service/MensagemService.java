package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.mappers.MensagemMapper;
import projetopi.projetopi.dto.request.MensagemCriacao;
import projetopi.projetopi.dto.response.ChatResposta;
import projetopi.projetopi.dto.response.MensagemResposta;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Chat;
import projetopi.projetopi.entity.Mensagem;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.repository.MensagemRepository;
import projetopi.projetopi.util.Global;

import java.util.List;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private Global global;

    @Autowired
    private MensagemMapper mapper;

    @Autowired
    private ImageService imageService;

    public MensagemResposta sendMessage(String token, Integer id, String mensagem, String tipo){
        boolean isUsuario = tipo.equalsIgnoreCase("usuario");
        Chat chat = chatService.getOrCreatedChat(token, id, isUsuario);
        Usuario usuario = global.getBarbeiroByToken(token);
        Mensagem novaMensagem = new Mensagem(chat, usuario, mensagem);
        return mapper.toDto(mensagemRepository.save(novaMensagem), chat);
    }

    public MensagemResposta sendMessage(String token, Integer id,  String mensagem, MultipartFile file,  String tipo){
        boolean isUsuario = tipo.equalsIgnoreCase("usuario");
        Chat chat = chatService.getOrCreatedChat(token, id, isUsuario);
        Usuario usuario = global.getBarbeiroByToken(token);
        Mensagem novaMensagem = new Mensagem(chat, usuario, mensagem);
        novaMensagem.setFilename(imageService.upload(file, "chat"));
        return mapper.toDto(mensagemRepository.save(novaMensagem), chat);
    }

    public List<ChatResposta> allChat(String token, boolean isUsuario){
        List<Chat> chats = chatService.allChat(token, isUsuario);

        if (chats.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }
        return mapper.toDtos(chats);
    }

    public List<MensagemResposta> openChat(String token, Integer id, String tipo){
        boolean isUsuario = tipo.equalsIgnoreCase("usuario");
        Chat chat = chatService.getOrCreatedChat(token, id, isUsuario);
        List<Mensagem> mensagens = mensagemRepository.findByChat_IdOrderByDataCriacaoDesc(chat.getId());
        return mapper.toDtos(mensagens, chat);
    }


}
