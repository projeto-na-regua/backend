package projetopi.projetopi.service;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.integration.IntegrationGraphEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import projetopi.projetopi.dto.response.ChatResposta;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.ChatRepository;
import projetopi.projetopi.repository.UsuarioRepository;
import projetopi.projetopi.util.Global;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Global global;

    public Chat getOrCreatedChat(String token, Integer id, boolean isUsuario) {
        global.validarToken(token);

        Integer idBarbearia = isUsuario ? id : global.getBarbeariaByToken(token).getId();
        Integer idUsuario = isUsuario ?  global.getBarbeiroByToken(token).getId() : id;

        Barbearia barbearia = barbeariasRepository.findById(idBarbearia)
                .orElseThrow(() -> new IllegalArgumentException("Barbearia não encontrada"));


        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrada"));

        Optional<Chat> chatExistente = chatRepository.findByUsuario_IdAndBarbearia_Id(idUsuario, barbearia.getId());

        if (chatExistente.isPresent()) {
            return chatExistente.get();
        }

        Chat novoChat = new Chat();
        novoChat.setUsuario(usuario);
        novoChat.setBarbearia(barbearia);
        novoChat.setDataCriacao(LocalDateTime.now()); // Define a data de criação

        return chatRepository.save(novoChat);
    }

    public List<Chat> allChat(String token, boolean isUsuario){
        global.validarToken(token);
        List<Chat> chats = isUsuario ? chatRepository.findByUsuario_Id(global.getBarbeiroByToken(token).getId())
                : chatRepository.findByBarbearia_Id(global.getBarbeariaByToken(token).getId());
        return chats;
    }

}
