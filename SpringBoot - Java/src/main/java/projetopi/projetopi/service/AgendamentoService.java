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
    private final UsuarioRepository usuarioRepository;

    private ModelMapper mapper;

    @Autowired
    private ImageService imageService;

    @Autowired
    private Token tk;

    @Autowired
    private Global global;

    private final Map<Integer, FilaHistorico> historicoPorCliente;

    @Autowired
    private NotificacoesService notificacoesService;

    private FilaHistorico getFilaHistoricoParaCliente(Integer clienteId) {
        return historicoPorCliente.computeIfAbsent(clienteId, k -> new FilaHistorico());
    }

    public String definirStatus(String status){
        return switch (status.toLowerCase()){
            case "pendente", "concluido", "agendado", "cancelado", "none" ->
                status.substring(0,1).toUpperCase() + status.substring(1).toLowerCase();
            default -> null;
        };
    }

    public List<AgendamentoConsulta> getAgendamento(String token, String inputStatus) {

        String status = Optional.ofNullable(definirStatus(inputStatus))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido"));

        Usuario usuario = usuarioRepository.findById(Integer.valueOf(tk.getUserIdByToken(token)))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", tk.getUserIdByToken(token)));


        List<Agendamento> agendamentos;
        if (status.equals("None")){

            agendamentos = Global.isBarbeiro(usuario)
                    ? repository.findByBarbeiroId(usuario.getId())
                    : repository.findByClienteId(usuario.getId());
        }else{
            agendamentos = Global.isBarbeiro(usuario)
                    ? repository.findByBarbeiroIdAndStatus(usuario.getId(), status)
                    : repository.findByClienteIdAndStatus(usuario.getId(), status);
        }

        if (agendamentos.isEmpty()) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento encontrado");

        return agendamentos.stream()
                .map(ag -> {
                    AgendamentoConsulta dto = AgendamentoMapper.toDto(ag);
                    if (ag.getBarbearia().getImgPerfil() != null && !ag.getBarbearia().getImgPerfil().isEmpty()) {
                        String imgPerfilUrl = imageService.getImgURL(ag.getBarbearia().getImgPerfil(), "barbearia");
                        dto.setImgPerfilBarbearia(imgPerfilUrl);
                    }

                    return dto;                })
                .collect(Collectors.toList());
    }



    public List<AgendamentoConsulta> getAgendamentoBarbearia(String token, String inputStatus) {

        String status = Optional.ofNullable(definirStatus(inputStatus))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido"));

        List<Agendamento> agendamentos = repository.findAgendamentosByBarbeariaIdAndStatus(global.getBarbeariaByToken(token).getId());

        if (agendamentos.isEmpty()) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento encontrado");

        return agendamentos.stream()
            .filter(ag -> !ag.getStatus().equalsIgnoreCase("Concluido"))
            .map(ag -> {
                AgendamentoConsulta dto = AgendamentoMapper.toDto(ag);
                if (ag.getBarbearia().getImgPerfil() != null && !ag.getBarbearia().getImgPerfil().isEmpty()) {
                    String imgPerfilUrl = imageService.getImgURL(ag.getBarbearia().getImgPerfil(), "barbearia");
                    dto.setImgPerfilBarbearia(imgPerfilUrl);
                }
                return dto;
        }).collect(Collectors.toList());
    }


    public AgendamentoConsulta getOneAgendamento(String token, Integer id){

        if (!usuarioRepository.existsById(Integer.valueOf(tk.getUserIdByToken(token)))) throw new AcessoNegadoException("Usuário");

        if (!repository.existsById(id)) throw new RecursoNaoEncontradoException("Agendamento", id);
        Agendamento agendamento = repository.findById(id).get();
        AgendamentoConsulta dto = AgendamentoMapper.toDto(agendamento);

        if (agendamento.getBarbearia().getImgPerfil() != null && !agendamento.getBarbearia().getImgPerfil().isEmpty()) {
            String imgPerfilUrl = imageService.getImgURL(agendamento.getBarbearia().getImgPerfil(), "barbearia");
            dto.setImgPerfilBarbearia(imgPerfilUrl);
        }
        return dto;
    }


    public List<HorarioDiaSemana> getHorarios(String token, BarbeiroServicoId barbeiroServicoId, LocalDate date){

        String dia3Letras = date.format(DateTimeFormatter.ofPattern("EEE", new Locale("pt")))
                .substring(0, 3).toUpperCase();

        DiaSemana diaSemana = diaSemanaRepository.findByNomeAndBarbeariaId(Dia.valueOf(dia3Letras), barbeiroServicoId.getBarbearia());

        Servico servico = servicoRepository.findById(barbeiroServicoId.getServico())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço", barbeiroServicoId.getServico()));

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


    public AgendamentoConsulta updateStatus(String token, Integer id, String inputStatus) {

        Usuario user = usuarioRepository.findById(Integer.valueOf(tk.getUserIdByToken(token)))
                .orElseThrow(() -> new AcessoNegadoException("Usuário"));

        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento", id));

        String newStatus = definirStatus(inputStatus);

        if (Global.isBarbeiro(user)) {
            global.validaBarbearia(token);
            if (newStatus.equalsIgnoreCase("Pendente") || newStatus.equalsIgnoreCase("None")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
            }

        } else {
            global.validaCliente(token, "Cliente");
            if (!newStatus.equalsIgnoreCase("Cancelado")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
            }
        }

        String currentStatus = agendamento.getStatus();

        boolean isValidCurrentStatus = "Pendente".equalsIgnoreCase(currentStatus) || "Agendado".equalsIgnoreCase(currentStatus);
        boolean isInvalidTransition = "Pendente".equalsIgnoreCase(currentStatus) && "Concluido".equalsIgnoreCase(newStatus);
        boolean isSameStatus = currentStatus.equalsIgnoreCase(newStatus);

        if (!isValidCurrentStatus || isInvalidTransition || isSameStatus) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        if (newStatus.equalsIgnoreCase("Concluido")) {
            agendamento.setDataHoraConcluido(LocalDateTime.now());
            AgendamentoConsulta agendamentoConsulta = AgendamentoMapper.toDto(agendamento);
            FilaHistorico fila = getFilaHistoricoParaCliente(user.getId());
            fila.adicionar(agendamentoConsulta);
        }

        agendamento.setStatus(newStatus);
        repository.save(agendamento);
        notificacoesService.notificarUpdateAgendamento(token, agendamento);
        return AgendamentoMapper.toDto(agendamento);
    }


    public AgendamentoConsulta adicionarAgendamento(AgendamentoCriacao a, String token){

        global.validaCliente(token, "Usuário");

        Cliente cliente = clienteRepository.findById(Integer.valueOf(tk.getUserIdByToken(token)))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", tk.getUserIdByToken(token)));

        Servico servico = servicoRepository.findById(a.getIdServico())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço", a.getIdServico()));

        Barbeiro barbeiro = barbeiroRepository.findById(a.getIdBarbeiro())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Barbeiro", a.getIdBarbeiro()));

        Barbearia barbearia = barbeariasRepository.findById(a.getIdBarbearia())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Barbearia", a.getIdBarbearia()));

        Agendamento nvAgendamento = repository.save(new Agendamento(a.getDataHora(), servico, barbeiro, cliente, barbearia));
        notificacoesService.notificarNovoAgendamento(token, nvAgendamento);
        return AgendamentoMapper.toDto(nvAgendamento);
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
            if (a.getBarbearia().getImgPerfil() != null && !a.getBarbearia().getImgPerfil().isEmpty()) {
                String imgPerfilUrl = imageService.getImgURL(a.getBarbearia().getImgPerfil(), "barbearia");
                agendamentoConsulta.setImgPerfilBarbearia(imgPerfilUrl);
            }
            fila.adicionar(agendamentoConsulta);
        }

        return new ArrayList<>(fila.getHistorico());
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
            if (a.getBarbearia().getImgPerfil() != null && !a.getBarbearia().getImgPerfil().isEmpty()) {
                String imgPerfilUrl = imageService.getImgURL(a.getBarbearia().getImgPerfil(), "barbearia");
                dto.setImgPerfilBarbearia(imgPerfilUrl);
            }
            dtos.add(dto);
        }
        return dtos;
    }


}
