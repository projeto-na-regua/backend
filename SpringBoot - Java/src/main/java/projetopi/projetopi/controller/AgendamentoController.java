package projetopi.projetopi.controller;

import com.azure.core.annotation.Get;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dto.request.AgendamentoCriacao;
import projetopi.projetopi.dto.response.AgendamentoConsulta;
import projetopi.projetopi.dto.response.DashboardConsulta;
import projetopi.projetopi.dto.response.HorarioDiaSemana;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.repository.AgendaRepository;
import projetopi.projetopi.service.AgendamentoService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
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
    public ResponseEntity<AgendamentoConsulta> adicionarAgendamento(@RequestHeader("Authorization") String token,
                                                                    @Valid @RequestBody AgendamentoCriacao nvAgendamento){
        return status(201).body(service.adicionarAgendamento(nvAgendamento, token));
    }

    @GetMapping("/list-all-by-status/{status}")
    public ResponseEntity<List<AgendamentoConsulta>> getAgendamentos(@RequestHeader("Authorization") String token,
                                                                    @PathVariable String status){
        return status(200).body(service.getAgendamento(token, status));
    }

    @GetMapping("/list-horarios-disponiveis")
    public ResponseEntity<List<HorarioDiaSemana>> getAgendamentos(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer barbeiro,
            @RequestParam Integer servico,
            @RequestParam Integer barbearia,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        BarbeiroServicoId barbeiroServico = new BarbeiroServicoId(barbeiro, servico, barbearia);
        return ResponseEntity.ok(service.getHorarios(token, barbeiroServico, date));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoConsulta> getOneAgendamento(@RequestHeader("Authorization") String token,
                                                                    @PathVariable Integer id){
        return status(200).body(service.getOneAgendamento(token, id));
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<AgendamentoConsulta> updateStatus(@RequestHeader("Authorization") String token,
                                                              @PathVariable Integer id, @PathVariable String status){
        return status(200).body(service.updateStatus(token, id, status));
    }

    @GetMapping("/historico")
    public ResponseEntity<List<AgendamentoConsulta>> getHistoricoPorCliente(@RequestHeader("Authorization") String token) {
        return status(200).body(service.getHistoricoPorCliente(token));
    }

    @GetMapping("/dashboard/metricas")
    public ResponseEntity<DashboardConsulta> getMetricasDash(@RequestHeader("Authorization") String token,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateInicial,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFinal){
        return status(200).body(service.getMetricasDash(token, dateInicial, dateFinal));

    }


    @GetMapping("/avaliacoes")
    public  ResponseEntity<List<AgendamentoConsulta>> getAllAvaliacoes(@RequestHeader("Authorization") String token){
        return ok().body(service.getAvaliacoes(token));
    }

    @PostMapping("/avaliar/{idAgendamento}")
    public  ResponseEntity<Avaliacao> getAllAvaliacoes(@RequestHeader("Authorization") String token,
                                                             @RequestBody Avaliacao a,
                                                             @PathVariable Integer idAgendamento){
        return ok().body(service.postAvaliacao(token, a, idAgendamento));
    }




}
