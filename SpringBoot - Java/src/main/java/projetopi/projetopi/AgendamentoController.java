package projetopi.projetopi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/agendamento")

public class AgendamentoController{

    private List<AgendaAux> agendamentos = new ArrayList<>();

    @PostMapping
    public ResponseEntity<AgendaAux> adicionarAgendamento(@RequestBody AgendaAux a){
        agendamentos.add(a);

        return ResponseEntity.status(200).body(a);
    }

    @GetMapping
    public ResponseEntity<List<AgendaAux>> getAgendamento(){
        return ResponseEntity.status(200).body(agendamentos);
    }

    @PatchMapping("{nomeBarbeiro}")
    public ResponseEntity<AgendaAux> atualizarAgendamento(@PathVariable String nomeBarbeiro){
        for(AgendaAux a : agendamentos){
            if(a.getNomeBarbeiro().equals(nomeBarbeiro)){
                a.setNomeBarbeiro("Jõao Careca");
                return ResponseEntity.status(200).body(a);
            }
        }
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping
    public ResponseEntity<List<AgendaAux>> deletarAgendamento(@PathVariable String nomeBarbeiro){
        for(AgendaAux a : agendamentos){
            if(a.getNomeBarbeiro().equals(nomeBarbeiro)){
                agendamentos.remove(a);
                return ResponseEntity.status(200).body(agendamentos);
            }
        }
        return ResponseEntity.status(200).build();
    }
}
