package projetopi.projetopi.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.AgendaAux;
import projetopi.projetopi.repositorio.AgendaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController{

    @Autowired
    private AgendaRepository repository;

    @PostMapping
    public ResponseEntity<AgendaAux> adicionarAgendamento(@RequestBody AgendaAux a){
        repository.save(a);
        return ResponseEntity.status(201).body(a);
    }

    @GetMapping
    public ResponseEntity<List<AgendaAux>> getAgendamento(){
        var lista = repository.findAll();
        return lista.isEmpty()
                ? status(204).build()
                : status(200).body(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<AgendaAux> atualizarAgendamento(@RequestBody AgendaAux a,
                                                          @PathVariable Integer id){
        if (repository.existsById(id)) {
            a.setId(id);
            repository.save(a);
            return status(200).body(a);
        }
        return status(404).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AgendaAux> deletarAgendamento(@PathVariable Integer id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return status(204).build();
        }
        return status(404).build();
    }

    @GetMapping("{nomeBarbeiro}")
    public ResponseEntity<List<AgendaAux>> agendamentosPorBarbeiro(@PathVariable String nomeBarbeiro){
        var lista = repository.findAllByNomeBarbeiro(nomeBarbeiro);
        return status(200).body(lista);
    }

    @GetMapping("{nomeCliente}")
    public ResponseEntity<List<AgendaAux>> agendamentosPorCliente(@PathVariable String nomeCliente){
        var lista = repository.findAllByNomeCliente(nomeCliente);
        return status(200).body(lista);
    }

    @GetMapping("{nomeBarbearia}")
    public ResponseEntity<List<AgendaAux>> agendamentosPorBarbearia(@PathVariable String nomeBarbearia){
        var lista = repository.findAllByNomeBarbearia(nomeBarbearia);
        return status(200).body(lista);
    }
}
