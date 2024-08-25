package projetopi.projetopi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dto.response.BarbeariaConsulta;
import projetopi.projetopi.dto.response.BarbeariaPesquisa;
import projetopi.projetopi.service.PesquisaService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin("*")
@RestController
@RequestMapping("/pesquisa")
public class PesquisaController {


    @Autowired
    private PesquisaService service;

    @GetMapping("/client-side")
    public ResponseEntity<List<BarbeariaPesquisa>> getBarbeariasByToken(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String servico,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestParam(required = false) Double raio) {

        return status(200).body(service.getAllByLocalizacao(token, servico, date, time, raio));
    }


    @GetMapping("/no-token")
    public ResponseEntity<List<BarbeariaPesquisa>> getBarbeariasByToken(
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String servico,
            @RequestParam(required = false) Double raio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {

        return ResponseEntity.status(200).body(service.getAllByLocalizacaoSemCadastro(servico, date, time, cep, raio));
    }

    @GetMapping("/client-side/filtro")
    public ResponseEntity<List<BarbeariaPesquisa>> getPerfilByCliente(@RequestHeader("Authorization") String token,
                                                                      @RequestParam String nomeBarbearia){
        return status(200).body(service.filtroBarberiasNome(token, nomeBarbearia));
    }
}
