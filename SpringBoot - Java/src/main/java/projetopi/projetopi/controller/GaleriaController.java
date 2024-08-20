package projetopi.projetopi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

import projetopi.projetopi.dto.request.GaleriaCriacao;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.entity.ImgsGaleria;
import projetopi.projetopi.service.GaleriaService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/galeria")
public class GaleriaController {

    @Autowired
    private GaleriaService service;
    @GetMapping
    public ResponseEntity<List<ImgsGaleria>> getImages(){
        return status(200).body(service.getImages());
    }

    @PostMapping
    public ResponseEntity<ImgsGaleria> uploadImge(String token, GaleriaCriacao criacao){
        return status(201).body(service.uploadImg(token, criacao));
    }


}
