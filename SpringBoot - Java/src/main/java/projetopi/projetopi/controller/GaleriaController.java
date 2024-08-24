package projetopi.projetopi.controller;


import com.azure.core.annotation.Put;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Recupera todas as imagens associadas ao cliente autenticado",
            description = "Este endpoint retorna uma lista de imagens associadas ao cliente autenticado. Se não houver imagens, retorna um status 204 No Content.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de imagens recuperada com sucesso."),
            @ApiResponse(responseCode = "204", description = "Nenhuma imagem encontrada.")
    })
    @GetMapping
    public ResponseEntity<List<GaleriaConsulta>> getImages(@RequestHeader("Authorization") String token){
        return status(200).body(service.getImages(token));
    }


    @Operation(summary = "Recupera uma imagem específica pelo ID",
            description = "Este endpoint retorna os detalhes de uma imagem específica identificada pelo ID. Se a imagem não for encontrada, retorna um status 404 Not Found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes da imagem recuperada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GaleriaConsulta> getOneImage(@RequestHeader("Authorization") String token,
                                                       @PathVariable Integer id){

        return status(200).body(service.getOneImageGalery(token, id));
    }


    @Operation(summary = "Faz o upload de uma nova imagem para o cliente autenticado",
            description = "Este endpoint permite o upload de uma nova imagem para o cliente autenticado. Retorna os detalhes da imagem carregada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imagem carregada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro no upload da imagem.")
    })
    @PostMapping
    public ResponseEntity<GaleriaConsulta> uploadImage(@RequestHeader("Authorization") String token,
                                                  @RequestParam("imagem") MultipartFile imagem,
                                                  @RequestParam("descricao") String descricao){
        return status(201).body(service.uploadImg(token, imagem, descricao));
    }


    @Operation(summary = "Marca uma imagem como inativa (exclui) pelo ID",
            description = "Este endpoint marca uma imagem como inativa, efetivamente excluindo-a da galeria. Retorna um status 201 Created em caso de sucesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagem marcada como inativa com sucesso."),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> uploadImage(@RequestHeader("Authorization") String token,
                                            @PathVariable Integer id) {
        service.delete(token, id);
        return ResponseEntity.status(204).build();
    }



}
