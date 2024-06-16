package projetopi.projetopi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dto.response.FinancaConsulta;
import projetopi.projetopi.entity.Financa;
import projetopi.projetopi.relatorios.RelatorioFinanceiro;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.FinanceiroRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import projetopi.projetopi.service.FinanceiroService;

import static org.springframework.http.ResponseEntity.status;


@CrossOrigin("*")
@RestController
@RequestMapping("/financas")
public class FinancaController {

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private BarbeariasRepository barbeariaRepository;

    @Autowired
    private FinanceiroService service;

    @GetMapping
    public ResponseEntity<FinancaConsulta> getFinancasPorBarbearia(@RequestHeader("Authorization") String token,
                                                                   @RequestParam Integer qtdDias,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        return status(200).body(service.getFinancasPorBarbearia(token, qtdDias, dataInicial, dataFinal));
    }

    @GetMapping("/especifico/{id}")
    public ResponseEntity<Optional<Financa>> getFinanca(@PathVariable Integer id){
        var financa = financeiroRepository.findById(id);

        if(!financeiroRepository.existsById(id)){
            return status(404).build();
        }

        return status(200).body(financa);
    }

    @GetMapping("/relatorio/{fk}")
    public void gerarRelatorioFinanca(@PathVariable Integer fk){
        var financas = financeiroRepository.findByBarbeariaId(fk);

        if(financas.isEmpty()){
            status(204).build();
        }

        RelatorioFinanceiro.gravarRelatorioFinanceiro(financas,"relatório_finanças");
        status(200);
    }

}
