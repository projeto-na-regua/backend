package projetopi.projetopi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetopi.projetopi.dto.request.FinancaCriacao;
import projetopi.projetopi.dto.response.FinancaConsulta;
import projetopi.projetopi.dto.response.TotalValorPorDia;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Financa;
import projetopi.projetopi.repository.AgendaRepository;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.FinanceiroRepository;
import projetopi.projetopi.repository.ServicoRepository;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

        Double total = servicoRepository.totalServicoByBarbearia(barbearia.getId(), dataInicialDateTime, dataFinalDateTime) == null ? 0. :
                servicoRepository.totalServicoByBarbearia(barbearia.getId(), dataInicialDateTime, dataFinalDateTime);

        List<TotalValorPorDia> servicos = agendaRepository.findByServicosByDataConcluido(barbearia.getId(), qtdDias);
        List<LocalDate> datas = new ArrayList<>();
        List<Double> precos = new ArrayList<>();

        String[][] servicosMatriz = new String[2][qtdDias];

        int index = 0;
        for (TotalValorPorDia t : servicos) {
            if (index < qtdDias) {
                datas.add(t.getData());
                precos.add(t.getPrecos());

                servicosMatriz[0][index] = t.getData().toString();
                servicosMatriz[1][index] = t.getPrecos().toString();

                index++;
            } else {
                break;
            }
        }
        FinancaConsulta financaConsulta = financeiroRepository.findByFinancasByBarbeariaIdAndBetweenDates(barbearia.getId(), dataInicialDateTime, dataFinalDateTime);

        financaConsulta.setReceita(financaConsulta.getReceita() == null ? 0. : financaConsulta.getReceita() + total);
        financaConsulta.setLucro(financaConsulta.getLucro() == null ? 0. : financaConsulta.getLucro() + total);
        financaConsulta.definiMatriz(qtdDias);
        financaConsulta.setServicos(servicosMatriz);


        financaConsulta.setLucratividade((financaConsulta.getLucro() / financaConsulta.getReceita() ) * 100);

        return financaConsulta;
    }


    public void postFinanca(String token, FinancaCriacao lancarFinanca) {

        global.validarBarbeiroAdm(token, "Barbeiro");
        global.validaBarbearia(token);

        Barbearia barbearia = global.getBarbeariaByToken(token);

        financeiroRepository.save( new Financa(lancarFinanca.getValor(), LocalDateTime.now(), barbearia, lancarFinanca.getDespesa()));


    }
}
