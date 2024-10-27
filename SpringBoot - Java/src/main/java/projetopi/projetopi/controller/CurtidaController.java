package projetopi.projetopi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.CurtidaResponse;
import projetopi.projetopi.dto.response.MensagemResposta;
import projetopi.projetopi.entity.Curtida;
import projetopi.projetopi.entity.Postagem;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.CurtidaRepository;
import projetopi.projetopi.repository.PostagemRepository;
import projetopi.projetopi.service.CurtidasService;
import projetopi.projetopi.util.Global;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/curtidas")
public class CurtidaController {

    @Autowired
    private CurtidasService service;

    @Operation(summary = "Curtir ou Descurtir Postagem/Comentário",
            description = "Alterna o estado de curtida de um usuário em uma postagem ou comentário. Se já curtido, remove a curtida.")
    @PostMapping
    private ResponseEntity<CurtidaResponse> curtir(
            @Parameter(description = "Token de autorização do usuário") @RequestHeader("Authorization") String token,
            @Parameter(description = "ID da postagem ou comentário para curtir") @RequestParam("id") Integer id,
            @Parameter(description = "Tipo de entidade: 'postagem' ou 'comentario'") @RequestParam("tipo") String tipo) {


        return status(201).body(service.curtir(token, id, tipo));
    }

    @Operation(summary = "Obter Curtida de Postagem/Comentário",
            description = "Retorna uma curtida específica de um usuário em uma postagem ou comentário.")
    @GetMapping
    public ResponseEntity<CurtidaResponse> getCurtida(
            @Parameter(description = "Token de autorização do usuário") @RequestHeader("Authorization") String token,
            @Parameter(description = "ID da postagem ou comentário para obter a curtida") @RequestParam("id") Integer id,
            @Parameter(description = "Tipo de entidade: 'postagem' ou 'comentario'") @RequestParam("tipo") String tipo) {

        CurtidaResponse response = service.getCurtida(token, id, tipo);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);  // Certifique-se de que este retorno seja compatível com JSON
    }

    @Operation(summary = "Listar Curtidas de Postagem/Comentário",
            description = "Lista todas as curtidas de uma postagem ou comentário específico.")
    @GetMapping("/listar")
    private ResponseEntity<List<CurtidaResponse>> listarCurtidas(
            @Parameter(description = "Token de autorização do usuário") @RequestHeader("Authorization") String token,
            @Parameter(description = "ID da postagem ou comentário para listar curtidas") @RequestParam("id") Integer id,
            @Parameter(description = "Tipo de entidade: 'postagem' ou 'comentario'") @RequestParam("tipo") String tipo) {

        List<CurtidaResponse> curtidas = service.listCurtida(token, id, tipo);
        return ResponseEntity.ok(curtidas);
    }

    @Operation(summary = "Obter Quantidade de Curtidas",
            description = "Retorna a quantidade total de curtidas para uma postagem ou comentário específico.")
    @GetMapping("/quantidade")
    private ResponseEntity<Integer> getQtdCurtidas(
            @Parameter(description = "Token de autorização do usuário") @RequestHeader("Authorization") String token,
            @Parameter(description = "ID da postagem ou comentário para contar curtidas") @RequestParam("id") Integer id,
            @Parameter(description = "Tipo de entidade: 'postagem' ou 'comentario'") @RequestParam("tipo") String tipo) {

        Integer quantidadeCurtidas = service.getQtdCurtidas(token, id, tipo);
        return ResponseEntity.ok(quantidadeCurtidas);
    }





}
