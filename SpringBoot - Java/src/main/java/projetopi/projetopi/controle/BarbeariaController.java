package projetopi.projetopi.controle;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.repositorio.BarbeariasRepository;
import projetopi.projetopi.repositorio.BarbeiroRepository;
import projetopi.projetopi.repositorio.EnderecoRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/barbearia")
public class BarbeariaController {

        @Autowired
        private BarbeiroRepository barbeiroRepository;

        @Autowired
        private BarbeariasRepository barbeariasRepository;

        @Autowired EnderecoRepository enderecoRepository;

        @GetMapping("/funcionarios/{fk}")
        public ResponseEntity<List<Barbeiro>> getFuncionarios(@PathVariable Integer fk){
            var barbeiros = barbeiroRepository.findByBarbeariaId(fk);
            return barbeiros.isEmpty() ? status(204).build() : status(200).body(barbeiros);
        }

        @PostMapping("/funcionarios/{fk}")
        public ResponseEntity<Barbeiro> postBarberiro(@PathVariable Integer fk, @Valid @RequestBody Barbeiro nvBarbeiro){

            if(barbeariasRepository.findById(fk).isEmpty()){
                return status(404).build();
            }

            nvBarbeiro.setBarbearia(barbeariasRepository.findById(fk).get());
            barbeiroRepository.save(nvBarbeiro);
            return status(201).body(nvBarbeiro);

        }


}

