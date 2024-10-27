package projetopi.projetopi.controller;


import com.azure.core.annotation.Get;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.MensagemCriacao;
import projetopi.projetopi.dto.response.ChatResposta;
import projetopi.projetopi.dto.response.MensagemResposta;
import projetopi.projetopi.entity.Mensagem;
import projetopi.projetopi.service.MensagemService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;


@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private MensagemService service;

    @PostMapping
    private ResponseEntity<MensagemResposta> enviar(
            @RequestHeader("Authorization") String token,
            @RequestParam("mensagem") String mensagem,
            @RequestParam("id") Integer id,
            @RequestParam( value = "imagem", required = false) MultipartFile file,
            @RequestParam("tipo") String tipo){
        return status(201).body(service.sendMessage(token, id, mensagem, file, tipo));
    }


    @GetMapping("/open-chat")
    private ResponseEntity<List<MensagemResposta>> openChatUser(@RequestHeader("Authorization") String token,
                                                            @RequestParam Integer id,
                                                            @RequestParam String tipo){
        return status(200).body(service.openChat(token, id, tipo));
    }

    @GetMapping("/user-side/all")
    private ResponseEntity<List<ChatResposta>> allChats(@RequestHeader("Authorization") String token){
        return status(200).body(service.allChat(token, true));
    }

    @GetMapping("/barbearia-side/all")
    private ResponseEntity<List<ChatResposta>> allChatsBarbearia(@RequestHeader("Authorization") String token){
        return status(200).body(service.allChat(token, false));
    }


}
