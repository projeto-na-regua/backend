package projetopi.projetopi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import projetopi.projetopi.dto.response.BarbeariaPesquisa;
import projetopi.projetopi.dto.response.FinancaConsulta;
import projetopi.projetopi.dto.response.TotalServicoPorDia;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Financa;
import projetopi.projetopi.relatorios.RelatorioFinanceiro;
import projetopi.projetopi.repository.AgendaRepository;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.FinanceiroRepository;
import projetopi.projetopi.repository.ServicoRepository;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;
@Service
@RequiredArgsConstructor
public class FinanceiroService {

    @Autowired
    private final FinanceiroRepository financeiroRepository;

    @Autowired
    private final BarbeariasRepository barbeariaRepository;

    @Autowired
    private final AgendaRepository agendaRepository;
    @Autowired
    private final ServicoRepository servicoRepository;

    @Autowired
    private Global global;

    @Autowired
    private Token tk;


    public FinancaConsulta getFinancasPorBarbearia(String token, Integer qtdDias,
                                                   LocalDate dataInicial, LocalDate dataFinal){

        global.validarBarbeiroAdm(token, "Barbeiro");
        global.validaBarbearia(token);

        LocalDateTime dataInicialDateTime = dataInicial.atStartOfDay();
        LocalDateTime dataFinalDateTime = dataFinal.atTime(LocalTime.MAX);

        Barbearia barbearia = global.getBarbeariaByToken(token);

        Double total = servicoRepository.totalServicoByBarbearia(barbearia.getId(), dataInicialDateTime, dataFinalDateTime);

        List<TotalServicoPorDia> servicos = agendaRepository.findByServicosByDataConcluido(barbearia.getId(), qtdDias);

        FinancaConsulta financaConsulta = financeiroRepository.findByFinancasByBarbeariaIdAndBetweenDates(barbearia.getId(), dataInicial, dataFinal);


        financaConsulta.setSaldo(financaConsulta.getSaldo() + total);
        financaConsulta.setLucro(financaConsulta.getLucro() + total);



        financaConsulta.setLucratividade((total / financaConsulta.getSaldo()) * 100);

        return financaConsulta;
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
