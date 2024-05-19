package projetopi.projetopi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projetopi.projetopi.entity.Financa;
import projetopi.projetopi.relatorios.RelatorioFinanceiro;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.FinanceiroRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import static org.springframework.http.ResponseEntity.status;


@CrossOrigin("*")
@RestController
@RequestMapping("/financas")
public class FinancaController {

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private BarbeariasRepository barbeariaRepository;

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
