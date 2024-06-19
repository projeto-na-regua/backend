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

    @GetMapping("/client-side/pesquisa-by-localizacao")
    public ResponseEntity<List<BarbeariaPesquisa>> getBarbeariasByToken(@RequestHeader("Authorization") String token,
                                                                        @RequestParam String servico,
                                                                        @RequestParam LocalDate date,
                                                                        @RequestParam LocalTime time){

        return status(200).body(service.getAllByLocalizacao(token, servico, date, time));
    }

    @GetMapping("/no-token/pesquisa-by-localizacao")
    public ResponseEntity<List<BarbeariaPesquisa>> getBarbeariasByToken(@RequestParam Double lat,
                                                                        @RequestParam Double lngt,
                                                                        @RequestParam String servico,
                                                                        @RequestParam LocalDate date,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)LocalTime time){

        return status(200).body(service.getAllByLocalizacaoSemCadastro(servico, date, time, lat, lngt));
    }

    @GetMapping("/client-side/perfil/{idBarbearia}")
    public ResponseEntity<BarbeariaConsulta> getPerfilByCliente(@RequestHeader("Authorization") String token,
                                                       @PathVariable Integer idBarbearia){
        return status(200).body(service.getPerfilForCliente(token, idBarbearia));
    }

    @GetMapping("/client-side/filtro")
    public ResponseEntity<List<BarbeariaPesquisa>> getPerfilByCliente(@RequestHeader("Authorization") String token,
                                                                @RequestParam String nomeBarbearia){
        return status(200).body(service.filtroBarberiasNome(token, nomeBarbearia));
    }

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

    @GetMapping("/client-side/get-one-image-perfil/{idBarbearia}")
    public ResponseEntity<String> getImagePerfilClienteSide(@RequestHeader("Authorization") String token, @PathVariable Integer idBarbearia){


//        ByteArrayResource resource = service.getImagePerfilClienteSide(token, idBarbearia);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.ok().body(service.getImagePerfilClienteSide(token, idBarbearia));
    }

    @GetMapping("/client-side/get-image-banner/{idBarbearia}")
    public ResponseEntity<String> getImageBanner(@RequestHeader("Authorization") String token, @PathVariable Integer idBarbearia){
//        ByteArrayResource resource = service.getImageBannerClieteSide(token, idBarbearia);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.ok().body(service.getImageBannerClieteSide(token, idBarbearia));
    }



    @GetMapping("/client-side/get-image-perfil")
    public ResponseEntity<List<String>> getImagePerfilCliente(@RequestHeader("Authorization") String token) {
        List<String> imageBytes = service.getImagePerfilCliente(token);

        return ResponseEntity.ok()
                .body(imageBytes);
    }

//    @GetMapping("/client-side/get-image-perfil/agendamento-concluido")
//    public ResponseEntity<List<String>> getImagePerfilClienteAgendamentoConcluido(@RequestHeader("Authorization") String token) {
//        List<String> imageBytes = service.getImagePerfilClienteAgendamentoConcluido(token);
//
//        return ResponseEntity.ok()
//                .body(imageBytes);
//    }

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


    @PutMapping("/editar-endereco/{id}")
    public ResponseEntity<Endereco> editarPerfilInfo(@Valid @RequestBody Endereco endereco, @PathVariable Integer id){
        return status(200).body(service.editarEndereco(endereco, id));
    }


    @GetMapping("/top-3-barbarias-avaliacoes")
    public ResponseEntity<List<BarbeariaAvaliacao>> getTop3MelhoreAvaliacoes(){
        return ok().body(service.getTop3Melhores());
    }



}
