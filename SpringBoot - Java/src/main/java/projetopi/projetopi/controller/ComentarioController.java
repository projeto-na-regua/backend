package projetopi.projetopi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.ComentarioConsulta;
import projetopi.projetopi.dto.response.PostConsulta;
import projetopi.projetopi.service.ComentarioService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService service;


    @PostMapping
    public ResponseEntity<ComentarioConsulta> comentar(@RequestHeader("Authorization") String token,
                                                       @RequestParam(value = "conteudo") String conteudo,
                        
                               @RequestParam(value = "idPostagem") Integer idPostagem,
                                                       @RequestParam(value = "midia", required = false) MultipartFile midia){

        return status(201).body(service.comentar(token, conteudo, midia, idPostagem));

    }

    @GetMapping
    public ResponseEntity<ComentarioConsulta> getComentario(@RequestHeader("Authorization") String token,
                                                @RequestParam(value = "id") Integer id){

        return status(201).body(service.getComentario(token, id));

    }

    @GetMapping("/by-post")
    public ResponseEntity<List<ComentarioConsulta>> getUltimosPost(@RequestHeader("Authorization") String token,
                                                                   @RequestParam(value = "idPostagem") Integer idPostagem){

        return status(200).body(service.getComentariosByPost(token, idPostagem));

    }


    @PutMapping("/delete")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token,
                                       @RequestParam(value = "id") Integer id){
        service.delete(token, id);
        return status(201).build();
    }

}