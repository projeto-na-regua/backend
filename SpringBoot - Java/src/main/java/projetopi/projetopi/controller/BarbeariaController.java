package projetopi.projetopi.controller;


import com.azure.core.annotation.Get;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.*;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.DiaSemana;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.DiaSemanaRepository;
import projetopi.projetopi.repository.EnderecoRepository;
import projetopi.projetopi.service.BarbeariaService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;
@CrossOrigin("*")
@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {

    @Autowired
    private BarbeariaService service;


    @GetMapping("/client-side/pesquisa")
    public ResponseEntity<List<BarbeariaPesquisa>> getBarbeariasByToken(@RequestHeader("Authorization") String token){
        return status(200).body(service.findAll(token));
    }


    @GetMapping("/perfil")
    public ResponseEntity<BarbeariaConsulta> getPerfil(@RequestHeader("Authorization") String token){
        return status(200).body(service.getPerfil(token));
    }


//    @GetMapping("/client-side/get-one-image-perfil/{idBarbearia}")
//    public ResponseEntity<String> getImagePerfilClienteSide(@RequestHeader("Authorization") String token, @PathVariable Integer idBarbearia){
//        return ResponseEntity.ok().body(service.getImagePerfilClienteSide(token, idBarbearia));
//    }
//
//    @GetMapping("/client-side/get-image-banner/{idBarbearia}")
//    public ResponseEntity<String> getImageBanner(@RequestHeader("Authorization") String token, @PathVariable Integer idBarbearia){
//        return ResponseEntity.ok().body(service.getImageBannerClieteSide(token, idBarbearia));
//    }
//
//
//    @GetMapping("/client-side/get-image-perfil")
//    public ResponseEntity<List<String>> getImagePerfilCliente(@RequestHeader("Authorization") String token) {
//        List<String> imageBytes = service.getImagePerfilCliente(token);
//
//        return ResponseEntity.ok()
//                .body(imageBytes);
//    }


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


    @GetMapping("/client-side/perfil/{idBarbearia}")
    public ResponseEntity<BarbeariaConsulta> getPerfilByCliente(@RequestHeader("Authorization") String token,
                                                                @PathVariable Integer idBarbearia){
        return status(200).body(service.getPerfilForCliente(token, idBarbearia));
    }




}
