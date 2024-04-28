package projetopi.projetopi.controle;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.relatorios.RelatorioBarbeiro;
import projetopi.projetopi.relatorios.RelatorioFinanceiro;
import projetopi.projetopi.repositorio.BarbeariasRepository;
import projetopi.projetopi.repositorio.BarbeiroRepository;
import projetopi.projetopi.repositorio.EnderecoRepository;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/funcionarios")
public class BarbeiroController {

        @Autowired
        private BarbeiroRepository barbeiroRepository;

        @Autowired
        private BarbeariasRepository barbeariasRepository;

        @Autowired EnderecoRepository enderecoRepository;

        @GetMapping("/{fk}")
        public ResponseEntity<List<Barbeiro>> getFuncionarios(@PathVariable Integer fk){

            if(!barbeariasRepository.existsById(fk)){
                return status(404).build();
            }

            var barbeiros = barbeiroRepository.findByBarbeariaId(fk);
            return barbeiros.isEmpty() ? status(204).build() : status(200).body(barbeiros);
        }

        @GetMapping("/especifico/{id}")
        public ResponseEntity<Barbeiro> getFuncionario(@PathVariable Integer id){
            return of(barbeiroRepository.findById(id));
        }

        @PostMapping("/{fk}")
        public ResponseEntity<Barbeiro> postBarberiro(@PathVariable Integer fk, @Valid @RequestBody Barbeiro nvBarbeiro){

            if(!barbeariasRepository.existsById(fk)){
                return status(404).build();
            }

            nvBarbeiro.setBarbearia(barbeariasRepository.getReferenceById(fk));
            barbeiroRepository.save(nvBarbeiro);
            return status(201).body(nvBarbeiro);

        }

        @PutMapping("/{fk}/{id}")
        public ResponseEntity<Barbeiro> editarBarbeiro(@Valid @RequestBody Barbeiro nvBarbeiro,
                                                       @PathVariable Integer fk,
                                                       @PathVariable Integer id){

            if(!barbeariasRepository.existsById(fk)){
                return status(404).build();
            }

            if(!barbeiroRepository.existsById(id)){
                return status(404).build();
            }

            if (barbeiroRepository.findByBarbeariaId(fk).isEmpty()){
                return status(404).build();
            }

            nvBarbeiro.setId(id);
            nvBarbeiro.setBarbearia(barbeariasRepository.getReferenceById(fk));
            barbeiroRepository.save(nvBarbeiro);
            return status(200).body(nvBarbeiro);
        }


        @DeleteMapping("/{fk}/{id}")
        public ResponseEntity<Barbeiro> deleteBarbeiro(@PathVariable Integer fk, @PathVariable Integer id){

            if (!barbeariasRepository.existsById(fk)){
                return status(404).build();
            }

            if (!barbeiroRepository.existsById(id)){
                return status(404).build();
            }

            if (barbeiroRepository.findByBarbeariaId(fk).isEmpty()){
                return status(404).build();
            }

            barbeiroRepository.deleteById(id);
            return status(200).build();
        }

        @GetMapping("/relatorio/{fk}")
        public void gerarRelatorioBarbeiro(@PathVariable Integer fk){
            var barbeiros = barbeiroRepository.findByBarbeariaId(fk);

            if(barbeiros.isEmpty()){
                status(204).build();
            }

            RelatorioBarbeiro.gravarRelatorioFinanceiro(barbeiros,"relatório_barbeiros");
            status(200);
        }


}

