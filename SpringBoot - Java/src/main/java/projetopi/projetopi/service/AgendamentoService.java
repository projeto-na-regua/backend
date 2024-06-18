package projetopi.projetopi.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.id.IntegralDataTypeHolder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.mappers.AgendamentoMapper;
import projetopi.projetopi.dto.request.AgendamentoCriacao;
import projetopi.projetopi.dto.response.*;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.exception.AcessoNegadoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Dia;
import projetopi.projetopi.util.FilaHistorico;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AgendamentoService {

    @Autowired
    private final AgendaRepository repository;

    @Autowired
    private final BarbeiroRepository barbeiroRepository;

    @Autowired
    private final ServicoRepository servicoRepository;

    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private final BarbeariasRepository barbeariasRepository;
    @Autowired
    private final DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private final AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private final BarbeiroServicoRepository barbeiroServicoRepository;
    private final UsuarioRepository usuarioRepository;

    private final FuncionarioService funcionarioService;

    private ModelMapper mapper;

    @Autowired
    private Global global;

    @Autowired
    private StorageService azureStorageService;

    @Autowired
    private Token tk;

    private final Map<Integer, FilaHistorico> historicoPorCliente;

    private FilaHistorico getFilaHistoricoParaCliente(Integer clienteId) {
        return historicoPorCliente.computeIfAbsent(clienteId, k -> new FilaHistorico());
    }

    public String definirStatus(String status){
        String stt = null;

        if (status.equalsIgnoreCase("Pendente")){
            stt = "Pendente";
        }else if (status.equalsIgnoreCase("Concluido")){
            stt = "Concluido";
        }else if (status.equalsIgnoreCase("Agendado")){
            stt = "Agendado";
        }else if (status.equalsIgnoreCase("Cancelado")){
            stt = "Cancelado";
        }

        return stt;
    }

    public List<AgendamentoConsulta> getAgendamento(String token, String status) {
        List<Agendamento> agendamentos = new ArrayList<>();


        if (!(status.equalsIgnoreCase("Pendente") ||
                status.equalsIgnoreCase("Agendado") ||
                status.equalsIgnoreCase("Concluido") ||
                status.equalsIgnoreCase("Cancelado") ||
                status.equalsIgnoreCase("none"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        String stt = definirStatus(status);


        Integer userId = Integer.valueOf(tk.getUserIdByToken(token));
        Usuario usuario = usuarioRepository.findById(userId).get();
        if (usuario.getDtype().equalsIgnoreCase("Barbeiro")) {
            Barbeiro barbeiro = barbeiroRepository.findById(userId).get();
            agendamentos = stt == null ? repository.findByBarbeiroId(barbeiro.getId())
                    : repository.findByBarbeiroIdAndStatus(barbeiro.getId(), stt);
        } else {
            Cliente cliente = clienteRepository.findById(userId).get();
            agendamentos = stt == null ? repository.findByClienteId(cliente.getId())
                    : repository.findByClienteIdAndStatus(cliente.getId(), stt);
        }


        if (agendamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento encontrado");
        }

        List<AgendamentoConsulta> dtos = new ArrayList<>();
        for (Agendamento a : agendamentos) {
                AgendamentoConsulta dto = AgendamentoMapper.toDto(a);
                dto.setImgPerfilBarbearia(azureStorageService.getBlobUrl(a.getBarbearia().getImgPerfil()));
                dtos.add(dto);

        }
        return dtos;
    }

    public List<AgendamentoConsulta> getAgendamentoBarbearia(String token, String status) {
        List<Agendamento> agendamentos;

        if (!(status.equalsIgnoreCase("Pendente") ||
                status.equalsIgnoreCase("Agendado") ||
                status.equalsIgnoreCase("Concluido") ||
                status.equalsIgnoreCase("Cancelado") ||
                status.equalsIgnoreCase("none"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        agendamentos = repository.findAgendamentosByBarbeariaIdAndStatus(global.getBarbeariaByToken(token).getId());
        System.out.println(agendamentos);

        if (agendamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento encontrado");
        }

        List<AgendamentoConsulta> dtos = new ArrayList<>();
        for (Agendamento a : agendamentos) {
            if(!(a.getStatus().equalsIgnoreCase("Concluido"))){
                AgendamentoConsulta dto = AgendamentoMapper.toDto(a);
                dto.setImgPerfilBarbearia(azureStorageService.getBlobUrl(a.getBarbearia().getImgPerfil()));
                dtos.add(dto);
            }
        }

        return dtos;
    }


    public AgendamentoConsulta getOneAgendamento(String token, Integer id){

        if (!usuarioRepository.existsById(Integer.valueOf(tk.getUserIdByToken(token)))){
            throw new AcessoNegadoException("Usuário");
        }

        if (!repository.existsById(id)){
            throw new RecursoNaoEncontradoException("Agendamento", id);
        }


        AgendamentoConsulta dto = AgendamentoMapper.toDto(repository.findById(id).get());
        dto.setImgPerfilBarbearia(azureStorageService.getBlobUrl(repository.findById(id).get().getBarbearia().getImgPerfil()));


        return dto;
    }

    public static void main(String[] args) {
        System.out.println(ChronoUnit.MINUTES.between(LocalTime.now(), LocalTime.now().plusMinutes(60)));
    }

    public List<HorarioDiaSemana> getHorarios(String token, BarbeiroServicoId barbeiroServicoId, LocalDate date){

        String dia3Letras = date.format(DateTimeFormatter.ofPattern("EEE", new Locale("pt")))
                .substring(0, 3).toUpperCase();

        DiaSemana diaSemana = diaSemanaRepository.findByNomeAndBarbeariaId(Dia.valueOf(dia3Letras), barbeiroServicoId.getBarbearia());

        Servico servico = servicoRepository.findById(barbeiroServicoId.getServico()).get();

        Integer tempoEstimado = servico.getTempoEstimado();

        long minutos = ChronoUnit.MINUTES.between(diaSemana.getHoraAbertura(), diaSemana.getHoraFechamento());

        long quantidadeHorarios = minutos / tempoEstimado;

        LocalTime horario = diaSemana.getHoraAbertura();
        List<HorarioDiaSemana> horariosDtos = new ArrayList<>();

        var formatoHM = DateTimeFormatter.ofPattern("HH:mm");

        for (int h = 1 ; h <= quantidadeHorarios; h++) {
            LocalDateTime dataHora = LocalDateTime.of(date, horario);
            if (repository.existsByBarbeiroServicoDataHoraConfirmado(
                    barbeiroServicoId.getBarbeiro(), barbeiroServicoId.getServico(), dataHora)) {
                horario = horario.plusMinutes(tempoEstimado);
                continue;
            }
            HorarioDiaSemana dto = new HorarioDiaSemana(
                    horario.format(formatoHM),
                    barbeiroServicoId.getBarbeiro(),
                    barbeiroServicoId.getServico()
            );
            horariosDtos.add(dto);
            horario = horario.plusMinutes(tempoEstimado);
        }
        return horariosDtos;
    }


    public AgendamentoConsulta updateStatus(String token, Integer id, String status) {
        Integer userId = Integer.valueOf(tk.getUserIdByToken(token));
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Agendamento", id);
        }

        if (!usuarioRepository.existsById(userId)) {
            throw new AcessoNegadoException("Usuário");
        }

        Agendamento agendamento = repository.findById(id).get();

        if (usuarioRepository.findById(userId).get().getDtype().equalsIgnoreCase("Barbeiro")) {
            global.validaBarbearia(token);
            if (!status.equalsIgnoreCase("Concluido")
                    && !status.equalsIgnoreCase("Agendado")
                    && !status.equalsIgnoreCase("Cancelado")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
            }
        } else {
            if (!status.equalsIgnoreCase("Cancelado")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
            }
        }

        if (!(agendamento.getStatus().equalsIgnoreCase("Pendente")) &&
                !(agendamento.getStatus().equalsIgnoreCase("Agendado"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        if (agendamento.getStatus().equalsIgnoreCase("Pendente")
                && definirStatus(status).equalsIgnoreCase("Concluido")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        if (agendamento.getStatus().equalsIgnoreCase(definirStatus(status))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        if (definirStatus(status).equalsIgnoreCase("Concluido")) {
            agendamento.setDataHoraConcluido(LocalDateTime.now());
            AgendamentoConsulta agendamentoConsulta = AgendamentoMapper.toDto(agendamento);
            FilaHistorico fila = getFilaHistoricoParaCliente(userId);
            fila.adicionar(agendamentoConsulta);
        }

        agendamento.setStatus(definirStatus(status));
        repository.save(agendamento);

        return AgendamentoMapper.toDto(agendamento);
    }


    public AgendamentoConsulta adicionarAgendamento(AgendamentoCriacao a, String token){

        global.validaCliente(token, "Usuário");
        Cliente cliente = clienteRepository.findById(Integer.valueOf(tk.getUserIdByToken(token))).get();


        if (!barbeariasRepository.existsById(a.getIdBarbearia())){
            throw new RecursoNaoEncontradoException("Barbearia", a.getIdBarbearia());
        }

        if (!barbeiroRepository.existsById(a.getIdBarbeiro())){
            throw new RecursoNaoEncontradoException("Barbeiro", a.getIdBarbeiro());
        }

        if (!servicoRepository.existsById(a.getIdServico())){
            throw new RecursoNaoEncontradoException("Serviço", a.getIdServico());
        }

        Servico servico = servicoRepository.findById(a.getIdServico()).get();
        Barbeiro barbeiro = barbeiroRepository.findById(a.getIdBarbeiro()).get();
        Barbearia barbearia = barbeariasRepository.findById(a.getIdBarbearia()).get();
        Agendamento nvAgendamento = new Agendamento(a.getDataHora(), servico, barbeiro, cliente, barbearia);
        nvAgendamento.setStatus("Pendente");


        return AgendamentoMapper.toDto(repository.save(nvAgendamento));
    }

    public List<AgendamentoConsulta> getHistoricoPorCliente(String token) {
        Integer userId = Integer.valueOf(tk.getUserIdByToken(token));
        if (!usuarioRepository.existsById(userId)) {
            throw new AcessoNegadoException("Usuário");
        }

        if (!clienteRepository.existsById(userId)) {
            throw new AcessoNegadoException("Cliente");
        }

        List<Agendamento> agendamentosConcluidos = repository.findByClienteIdAndStatus(userId, "Concluido");

        if (agendamentosConcluidos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento concluído encontrado");
        }

        FilaHistorico fila = getFilaHistoricoParaCliente(userId);

        fila.getFila().clear();

        for (Agendamento a : agendamentosConcluidos) {
            AgendamentoConsulta agendamentoConsulta = AgendamentoMapper.toDto(a);
            agendamentoConsulta.setImgPerfilBarbearia(azureStorageService.getBlobUrl(a.getBarbearia().getImgPerfil()));
            fila.adicionar(agendamentoConsulta);
        }

        return new ArrayList<>(fila.getHistorico());
    }

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

    public List<AvaliacaoConsulta> getAvaliacoes(String token, Integer quantidade) {

        global.validarBarbeiroAdm(token, "Barbeiro");
        global.validaBarbearia(token);
        Barbearia barbearia = global.getBarbeariaByToken(token);


        List<AvaliacaoConsulta> dto = repository.findUltimasAvaliacoes(barbearia.getId(), quantidade);

        if (dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento concluído encontrado");
        }


        return dto;
    }

    public List<AvaliacaoConsulta> getAvaliacoesClienteSide(String token, Integer quantidade, Integer idBarbearia) {

        global.validaCliente(token, "Cliente");

        if (!barbeariasRepository.existsById(idBarbearia)){
            throw new RecursoNaoEncontradoException("Barbearia", idBarbearia);
        }
        Barbearia barbearia = barbeariasRepository.findById(idBarbearia).get();


        List<AvaliacaoConsulta> dto = repository.findUltimasAvaliacoes(barbearia.getId(), quantidade);

        if (dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento concluído encontrado");
        }

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



    public List<AgendamentoConsulta> getAvaliacoes(String token) {
        global.validaCliente(token, "Cliente");
        Integer id = Integer.valueOf(tk.getUserIdByToken(token));

        if (!clienteRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var lista = repository.findByClienteIdAndStatusAndAvaliacaoIsNull(id, "Concluido");

        if (lista.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        List<AgendamentoConsulta> dtos = new ArrayList<>();
        for (Agendamento a : lista){
            AgendamentoConsulta dto = AgendamentoMapper.toDto(a);
            dto.setImgPerfilBarbearia(azureStorageService.getBlobUrl(a.getBarbearia().getImgPerfil()));
            dtos.add(dto);
        }
        return dtos;
    }

    public Avaliacao postAvaliacao(String token, Avaliacao a, Integer idAgendamento){
        global.validaCliente(token, "Cliente");


        Avaliacao avaliacaoSalva = avaliacaoRepository.save(a);

        if (!repository.existsById(idAgendamento)){
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Agendamento agendamento = repository.findById(idAgendamento).get();


        agendamento.setAvaliacao(avaliacaoSalva);
        repository.save(agendamento);
        return avaliacaoSalva;
    }
}
