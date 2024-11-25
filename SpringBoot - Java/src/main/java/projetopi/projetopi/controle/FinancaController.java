package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Financa;
import projetopi.projetopi.relatorios.RelatorioFinanceiro;
import projetopi.projetopi.repositorio.BarbeariasRepository;
import projetopi.projetopi.repositorio.FinanceiroRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;


@CrossOrigin("*")
@RestController
@RequestMapping("/financas")
public class FinancaController {

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private BarbeariasRepository barbeariaRepository;

    @PostMapping("/lancamento")
    public ResponseEntity<Financa> criarLancamentoFinanceiro(@Valid @RequestBody Financa lancamento) {
        if (!barbeariaRepository.existsById(lancamento.getBarbeariaId())) {
            return ResponseEntity.status(404).build();
        }

        Financa novoLancamento = financeiroRepository.save(lancamento);
        return ResponseEntity.status(201).body(novoLancamento);
    }

    @GetMapping("/{fk}")
    public ResponseEntity<List<Financa>> getFinancasPorBarbearia(@PathVariable Integer fk){

        if(!financeiroRepository.existsById(fk)){
            return status(404).build();
        }

        var financas = financeiroRepository.findByBarbeariaId(fk);
        return financas.isEmpty() ? status(204).build() : status(200).body(financas);
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
