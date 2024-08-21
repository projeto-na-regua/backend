package projetopi.projetopi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.GaleriaConsulta;
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
    public ResponseEntity<List<GaleriaConsulta>> getImages(@RequestHeader("Authorization") String token){
        return status(200).body(service.getImages(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GaleriaConsulta> getOneImage(@RequestHeader("Authorization") String token,
                                                       @PathVariable Integer id){

        return status(200).body(service.getOneImageGalery(token, id));
    }

    @PostMapping
    public ResponseEntity<GaleriaConsulta> uploadImage(@RequestHeader("Authorization") String token,
                                                  @RequestParam("imagem") MultipartFile imagem,
                                                  @RequestParam("descricao") String descricao){

        return status(201).body(service.uploadImg(token, imagem, descricao));
    }


}
