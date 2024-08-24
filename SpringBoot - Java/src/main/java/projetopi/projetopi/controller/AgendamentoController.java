package projetopi.projetopi.controller;

import com.azure.core.annotation.Get;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dto.request.AgendamentoCriacao;
import projetopi.projetopi.dto.response.*;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.repository.AgendaRepository;
import projetopi.projetopi.service.AgendamentoService;
import projetopi.projetopi.service.AvaliacaoService;
import projetopi.projetopi.service.DashboardService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@CrossOrigin("*")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController{

    @Autowired
    private AgendamentoService service;

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private DashboardService dashboardService;

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

    @GetMapping("/list-all-by-status-all-barbearia/{status}")
    public ResponseEntity<List<AgendamentoConsulta>> getAgendamentosBarbearia(@RequestHeader("Authorization") String token, @PathVariable String status){
        return status(200).body(service.getAgendamentoBarbearia(token, status));
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
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFinal,
                                                             @RequestParam Integer qtdDiasParaGrafico){

        return status(200).body(dashboardService.getMetricasDash(token, dateInicial, dateFinal, qtdDiasParaGrafico));

    }


    @GetMapping("/avaliacoes")
    public  ResponseEntity<List<AgendamentoConsulta>> getAllAvaliacoes(@RequestHeader("Authorization") String token){
        return ok().body(service.getAvaliacoes(token));
    }

    @PostMapping("/avaliar/{idAgendamento}")
    public  ResponseEntity<Avaliacao> getAllAvaliacoes(@RequestHeader("Authorization") String token,
                                                             @RequestBody Avaliacao a,
                                                             @PathVariable Integer idAgendamento){
        return ok().body(avaliacaoService.postAvaliacao(token, a, idAgendamento));
    }


    @GetMapping("/dashboard/ultimas-avaliacoes/{qtd}")
    public ResponseEntity<List<AvaliacaoConsulta>> getAgendamentos(
            @RequestHeader("Authorization") String token, @PathVariable Integer qtd) {

        return ResponseEntity.ok(avaliacaoService.getAvaliacoes(token, qtd));
    }

    @GetMapping("/cliente-side/ultimas-avaliacoes")
    public ResponseEntity<List<AvaliacaoConsulta>> getAgendamentos(
            @RequestHeader("Authorization") String token, @RequestParam Integer qtd, @RequestParam Integer idBarbearia) {

        return ResponseEntity.ok(avaliacaoService.getAvaliacoesClienteSide(token, qtd, idBarbearia));
    }

//    @GetMapping("/cliente-side/all-ultimas-avaliacoes")
//    public ResponseEntity<List<AvaliacaoConsulta>> getAllAvaliacoes(
//            @RequestHeader("Authorization") String token,
//            @RequestParam Integer qtd,
//            @RequestParam List<Integer> idBarbearias) {
//
//        return ResponseEntity.ok(service.getAllAvaliacoesClienteSide(token, qtd, idBarbearias));
//    }



}
