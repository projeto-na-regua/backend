package projetopi.projetopi.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Agendamento;
import projetopi.projetopi.repositorio.AgendaRepository;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin("*")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController{

    @Autowired
    private AgendaRepository repository;


    @PostMapping
    public ResponseEntity<Agendamento> adicionarAgendamento(@RequestBody Agendamento a){
        repository.save(a);
        return ResponseEntity.status(201).body(a);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> getAgendamento(){
        var lista = repository.findAll();
        return lista.isEmpty()
                ? status(204).build()
                : status(200).body(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizarAgendamento(@RequestBody Agendamento a,
                                                            @PathVariable Integer id){
        if (repository.existsById(id)) {
            a.setId(id);
            repository.save(a);
            return status(200).body(a);
        }
        return status(404).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Agendamento> deletarAgendamento(@PathVariable Integer id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return status(204).build();
        }
        return status(404).build();
    }

}
