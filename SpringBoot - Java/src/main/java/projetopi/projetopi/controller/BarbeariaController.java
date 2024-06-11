package projetopi.projetopi.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.DiaSemana;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.dto.response.BarbeariaConsulta;
import projetopi.projetopi.dto.response.EnderecoConsulta;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.DiaSemanaRepository;
import projetopi.projetopi.repository.EnderecoRepository;
import projetopi.projetopi.service.BarbeariaService;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;
@CrossOrigin("*")
@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {



    @Autowired
    private BarbeariaService service;
    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;


    @GetMapping("/perfil")
    public ResponseEntity<BarbeariaConsulta> getPerfil(@RequestHeader("Authorization") String token){
        return status(200).body(service.getPerfil(token));
    }

    @GetMapping("/get-image-perfil")
    public ResponseEntity<ByteArrayResource> getImagePerfil(@RequestHeader("Authorization") String token){
        ByteArrayResource resource = service.getImagePerfil(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).body(resource);
    }

    @GetMapping("/get-image-banner")
    public ResponseEntity<ByteArrayResource> getImageBanner(@RequestHeader("Authorization") String token){
        ByteArrayResource resource = service.getImageBanner(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).body(resource);
    }

    @PutMapping("/image-perfil")
    public ResponseEntity<ImgConsulta> getPerfil(@RequestHeader("Authorization") String token,
                                                 @RequestParam("file") MultipartFile file){
        return status(200).body(service.editarImgPerfil(token, file));
    }

    @PutMapping("/image-banner")
    public ResponseEntity<ImgConsulta> getBanner(@RequestHeader("Authorization") String token,
                                                 @RequestParam("file") MultipartFile file){
        return status(200).body(service.editarImgBanner(token, file));
    }


    @PutMapping("/perfil")
    public ResponseEntity<BarbeariaConsulta> editarPerfilInfo(@RequestHeader("Authorization") String token, @Valid
                                                              @RequestBody BarbeariaConsulta nvBarbearia){
        return status(200).body(service.editarPerfilInfo(token, nvBarbearia));
    }



}
