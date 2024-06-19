package projetopi.projetopi.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dto.request.ServicoCriacao;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.entity.BarbeiroServico;
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

    @GetMapping("/client-side/{idBarbearia}")
    public ResponseEntity<List<ServicoConsulta>> getServicos(@RequestHeader("Authorization") String token,
                                                             @PathVariable Integer idBarbearia){
        return status(200).body(service.getAllServicosByBarbeariaForClientes(token, idBarbearia));
    }


    @GetMapping("/list-by-status/{status}")
    @Operation(summary = "Listando os serviços pelo status", description = """
               status = active ou deactive
               """)
    public ResponseEntity<List<ServicoConsulta>> getServicesByStatus(@RequestHeader("Authorization") String token, @PathVariable String status){
        return status(200).body(service.getAllServicosByStatus(token, status));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ServicoConsulta> getServicoEspecifico(@RequestHeader("Authorization") String token,
                                                                @PathVariable Integer id){
        return status(200).body(service.getServico(token, id));
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<List<ServicoConsulta>> findByName(@RequestHeader("Authorization") String token,
                                                                @RequestParam String nomeServico){
        return status(200).body(service.findByName(token, nomeServico));
    }

    @PutMapping("/disassociate-barbeiro/{email}/{id}")
    public ResponseEntity<BarbeiroServico> desassociarBarbeiro(@RequestHeader("Authorization") String token,
                                                               @PathVariable String email,
                                                               @PathVariable Integer id){
        service.deletarRelacaoServicoBarbeiro(token, id, email);
        return status(204).build();
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

    @PutMapping("/update-status/{id}")
    public ResponseEntity<ServicoConsulta> updateStatusServivco(@RequestHeader("Authorization") String token,
                                                          @PathVariable Integer id){

        return status(200).body(service.updateStatus(token, id));
    }

// Na regra de negócio não iremos efetivamente deletar um serviço e sim desativá-lo através de um PUT no status.
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ServicoConsulta> deletarServico(@RequestHeader("Authorization") String token, @PathVariable Integer id){
//        service.deletar(token, id);
//        return status(200).build();
//    }



}
