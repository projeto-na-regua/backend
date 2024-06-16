package projetopi.projetopi.controller;


import jakarta.validation.Valid;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.dto.request.BarbeiroCriacao;
import projetopi.projetopi.dto.response.BarbeiroConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.relatorios.RelatorioBarbeiro;
import projetopi.projetopi.service.FuncionarioService;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;
@CrossOrigin("*")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

        @Autowired
        private FuncionarioService service;

        @GetMapping
        public ResponseEntity<List<BarbeiroConsulta>> getFuncionarios(@RequestHeader("Authorization") String token){
            return status(200).body(service.getFuncionarios(token));
        }

        @GetMapping("/client-side/{idBarbearia}")
        public ResponseEntity<List<BarbeiroConsulta>> getFuncionariosCliente(@RequestHeader("Authorization") String token, @PathVariable Integer idBarbearia){
            return status(200).body(service.getFuncionariosCliente(token, idBarbearia));
        }
        @GetMapping("/list-by-servico/{idServico}")
        public ResponseEntity<List<BarbeiroConsulta>> getFuncionarios(@RequestHeader("Authorization") String token,
                                                                      @PathVariable Integer idServico){
            return status(200).body(service.getFuncionariosByServico(token, idServico));
        }

        @GetMapping("/client-side/list-by-servico/{idServico}")
        public ResponseEntity<List<BarbeiroConsulta>> getFuncionariosForCliente(@RequestHeader("Authorization") String token,
                                                                  @PathVariable Integer idServico){
        return status(200).body(service.getFuncionariosByServicoForCliente(token, idServico));
        }


        @GetMapping("/{email}")
        public ResponseEntity<BarbeiroConsulta> getFuncionario(@RequestHeader("Authorization") String token,
                                                               @PathVariable String email){
            return status(200).body(service.getFuncionarioEspecifico(email, token));

        }

        @PostMapping("/criar")
        public ResponseEntity<BarbeiroConsulta> criarBarbeiro(@RequestHeader("Authorization") String token,
                                                              @Valid @RequestBody BarbeiroCriacao barbeiro){
            return status(201).body(service.criarBarbeiro(token, barbeiro));

        }

        @GetMapping("/get-usuarios-by-email/{email}")
        public ResponseEntity<UsuarioConsulta> getUsersByEmail(@RequestHeader("Authorization") String token,
                                                               @PathVariable String email){

            return  status(200).body(service.buscarEmailBarbeiro(email, token));

        }

        @PutMapping("/adicionar/{email}")
        public ResponseEntity<BarbeiroConsulta> adicionarBarberbeiro(@RequestHeader("Authorization") String token,
                                                            @PathVariable String email){
            service.adicionarBarberiro(token, email);
            return status(204).build();

        }



        @DeleteMapping("/{email}")
        public ResponseEntity<BarbeiroConsulta> deleteBarbeiro(@RequestHeader("Authorization") String token,
                                                               @PathVariable String email){
            service.deleteBarbeiro(email, token);
            return status(204).build();
        }


        @GetMapping("/relatorio")
        public ResponseEntity<byte[]> gerarRelatorioBarbeiro(@RequestHeader("Authorization") String token) {
            return new ResponseEntity<>(service.gerarRelatorio(token), service.configurarHeadears(), HttpStatus.OK);
        }
}

