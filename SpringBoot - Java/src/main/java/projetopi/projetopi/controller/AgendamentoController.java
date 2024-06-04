package projetopi.projetopi.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dto.mappers.ServicoMapper;
import projetopi.projetopi.dto.request.AgendamentoCriacao;
import projetopi.projetopi.dto.response.AgendamentoConsulta;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.repository.AgendaRepository;
import projetopi.projetopi.repository.BarbeiroServicoRepository;
import projetopi.projetopi.repository.ServicoRepository;
import projetopi.projetopi.service.AgendamentoService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin("*")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController{

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private AgendamentoService service;



    @PostMapping
    public ResponseEntity<AgendamentoConsulta> adicionarAgendamento(@Valid  @RequestBody AgendamentoCriacao nvAgendamento){
        return status(201).body(service.adicionarAgendamento(nvAgendamento));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoConsulta>> getAgendamento(){
        return status(200).body(service.getAgendamento());
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
