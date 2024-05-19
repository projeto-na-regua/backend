package projetopi.projetopi.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.entity.Servico;
import projetopi.projetopi.dto.request.ServicoCriacao;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.BarbeiroRepository;
import projetopi.projetopi.repository.ServicoRepository;
import projetopi.projetopi.service.ServicoService;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;
@CrossOrigin("*")
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService service;


    @GetMapping()
    public ResponseEntity<List<ServicoConsulta>> getServicos(@RequestHeader("Authorization") String token){
        return status(200).body(service.getAllServicos(token));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ServicoConsulta> getServicoEspecifico(@RequestHeader("Authorization") String token,
                                                                @PathVariable Integer id){
        return status(200).body(service.getServico(token, id));
    }


    @PostMapping
    public ResponseEntity<ServicoConsulta> cadastrarServivco(@RequestHeader("Authorization") String token,
                                                             @Valid @RequestBody ServicoCriacao nvServico){
        return status(201).body(service.criar(token, nvServico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoConsulta> editarServivco(@RequestHeader("Authorization") String token,
                                                          @PathVariable Integer id,
                                                          @Valid @RequestBody ServicoCriacao nvServico){
        return status(200).body(service.atualizar(token, id, nvServico));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ServicoConsulta> deletarServico(@RequestHeader("Authorization") String token,
                                                          @PathVariable Integer id){
        service.deletar(token, id);
        return status(200).build();
    }



}
