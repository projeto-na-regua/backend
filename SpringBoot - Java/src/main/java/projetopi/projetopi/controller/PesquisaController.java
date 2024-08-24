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
    public ResponseEntity<List<BarbeariaPesquisa>> getBarbeariasByToken(@RequestHeader("Authorization") String token,
                                                                        @RequestParam String servico,
                                                                        @RequestParam LocalDate date,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time ,
                                                                        @RequestParam Double raio){

        return status(200).body(service.getAllByLocalizacao(token, servico, date, time, raio));
    }

    @GetMapping("/no-token")
    public ResponseEntity<List<BarbeariaPesquisa>> getBarbeariasByToken(@RequestParam String cep,
                                                                        @RequestParam String servico,
                                                                        @RequestParam Double raio,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)LocalTime time){

        return status(200).body(service.getAllByLocalizacaoSemCadastro(servico, date, time, cep, raio));
    }

    @GetMapping("/client-side/filtro")
    public ResponseEntity<List<BarbeariaPesquisa>> getPerfilByCliente(@RequestHeader("Authorization") String token,
                                                                      @RequestParam String nomeBarbearia){
        return status(200).body(service.filtroBarberiasNome(token, nomeBarbearia));
    }
}
