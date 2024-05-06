package projetopi.projetopi.controle;


import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dto.request.BarbeiroCriacao;
import projetopi.projetopi.dto.response.BarbeiroConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.relatorios.RelatorioBarbeiro;
import projetopi.projetopi.service.FuncionarioService;

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
            if (!service.validarPermissioes(token)) return status(403).build();
            List<BarbeiroConsulta> barbeiros = service.getFuncionarios(token);
            return barbeiros.isEmpty() ? status(204).build() : status(200).body(barbeiros);

        }

        @GetMapping("/especifico/{email}")
        public ResponseEntity<BarbeiroConsulta> getFuncionario(@RequestHeader("Authorization") String token, @PathVariable String email){

            if (!service.validarPermissioes(token)) return status(403).build();
            if(!service.barbeiroExiste(email)) return status(404).build();
            return status(200).body(service.getFuncionarioEspecifico(email));

        }

        @PostMapping("/criar")
        public ResponseEntity<BarbeiroConsulta> criarBarbeiro(@RequestHeader("Authorization") String token, @RequestBody BarbeiroCriacao barbeiro){

            if (!service.validarPermissioes(token)) return status(403).build();
            if(service.barbeiroExiste(barbeiro.getEmail())) return status(409).build();
            return status(201).body(service.criarBarbeiro(token, barbeiro));

        }

        @GetMapping("/get-usuarios-by-email/{email}")
        public ResponseEntity<UsuarioConsulta> getUsersByEmail(@RequestHeader("Authorization") String token, @PathVariable String email){

            if (!service.validarPermissioes(token)) return status(403).build();
            if(service.userComumExiste(email)) return status(404).build();
            return  status(200).body(service.buscarEmailBarbeiro(email));

        }

        @PutMapping("/adicionar/{email}")
        public ResponseEntity<BarbeiroConsulta> adicionarBarberbeiro(@RequestHeader("Authorization") String token,
                                                            @PathVariable String email){

            if (!service.validarPermissioes(token)) return status(403).build();
            if(service.userComumExiste(email)) return status(404).build();
            service.adicionarBarberiro(token, email);
            return status(204).build();

        }



        @DeleteMapping("/{email}")
        public ResponseEntity<BarbeiroConsulta> deleteBarbeiro(@RequestHeader("Authorization") String token, @PathVariable String email){

            if (!service.validarPermissioes(token)) return status(403).build();
            if(!service.barbeiroExiste(email)) return status(404).build();
            service.deleteBarbeiro(email);
            return status(204).build();
        }


        @GetMapping("/relatorio")
        public ResponseEntity<Barbeiro> gerarRelatorioBarbeiro(@RequestHeader("Authorization") String token){

            if (!service.validarPermissioes(token)) return status(403).build();

            List<Barbeiro> barbeiros = service.mapper.map(service.getFuncionarios(token), new TypeToken<List<Barbeiro>>(){}.getType());
            RelatorioBarbeiro.gravarRelatorioFinanceiro(barbeiros,"relatório_barbeiros");
            return status(204).build();
        }


}

