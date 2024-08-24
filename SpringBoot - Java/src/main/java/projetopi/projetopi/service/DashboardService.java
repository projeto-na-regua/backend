package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetopi.projetopi.dto.response.DashboardConsulta;
import projetopi.projetopi.dto.response.TotalValorPorDia;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.repository.AgendaRepository;
import projetopi.projetopi.util.Global;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {


    @Autowired
    private AgendaRepository repository;

    @Autowired
    private Global global;

    public DashboardConsulta getMetricasDash(String token, LocalDate dateInicial, LocalDate dateFinal, Integer qtdDias) {

        global.validarBarbeiroAdm(token, "Barbeiro");
        global.validaBarbearia(token);
        Barbearia barbearia = global.getBarbeariaByToken(token);

        LocalDateTime dataInicialDateTime = dateInicial.atStartOfDay();
        LocalDateTime dataFinalDateTime = dateFinal.atTime(LocalTime.MAX);

        List<TotalValorPorDia> agendamentosdByDay = countConcluidoByDayForLastDays(barbearia.getId(), qtdDias);

        List<LocalDate> datas = new ArrayList<>();
        List<Long> precos = new ArrayList<>();

        for(TotalValorPorDia a : agendamentosdByDay){
            datas.add(a.getData());
            precos.add(a.getTotal());
        }

        DashboardConsulta dto = repository.findDashboardData(barbearia.getId(), dataInicialDateTime, dataFinalDateTime);

        dto.setMediaAvaliacoes(repository.findAverageResultadoAvaliacao(barbearia.getId()));
        dto.setValoresGrafico(precos);
        dto.setDatasGrafico(datas);


        return dto;
    }

    public List<TotalValorPorDia> countConcluidoByDayForLastDays(Integer barbeariaId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minus(days, ChronoUnit.DAYS);
        List<Object[]> results = repository.debugCountConcluidoByDay(barbeariaId, startDate);

        return results.stream().map(result -> {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Long count = (Long) result[1];
            return new TotalValorPorDia(date, count);
        }).collect(Collectors.toList());
    }
}
