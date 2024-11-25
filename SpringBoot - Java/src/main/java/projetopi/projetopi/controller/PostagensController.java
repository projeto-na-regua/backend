package projetopi.projetopi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.PostConsulta;
import projetopi.projetopi.entity.Postagem;
import projetopi.projetopi.service.PostagemService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/postagens")
public class PostagensController {

    @Autowired
    private PostagemService service;

    @PostMapping
    public ResponseEntity<PostConsulta> postar(@RequestHeader("Authorization") String token,
                                               @RequestParam(value = "descricao") String descricao,
                                               @RequestParam(value = "midia", required = false) MultipartFile midia
                                           ){

        return status(201).body(service.postar(token, descricao, midia));

    }

    @GetMapping
    public ResponseEntity<PostConsulta> getPost(@RequestHeader("Authorization") String token,
                                               @RequestParam(value = "id") Integer id){

        return status(201).body(service.getPost(token, id));

    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostConsulta>> getUltimosPost(@RequestHeader("Authorization") String token){

        return status(200).body(service.getUltimosPosts(token));

    }

    @GetMapping("/meus")
    public ResponseEntity<List<PostConsulta>> getMyPosts(@RequestHeader("Authorization") String token ){

        return status(200).body(service.getMyPosts(token));
    }

    @GetMapping("/by-id-user")
    public ResponseEntity<List<PostConsulta>> gePostsByUsuario(@RequestHeader("Authorization") String token,
                                                               @RequestParam(value = "id") Integer id){

        return status(200).body(service.getByUsuario(token, id));
    }

    @PutMapping("/delete")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token,
                                                     @RequestParam(value = "id") Integer id){
        service.deletePost(token, id);
        return status(201).build();
    }


}
