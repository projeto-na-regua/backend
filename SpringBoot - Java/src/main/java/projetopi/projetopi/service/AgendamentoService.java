package projetopi.projetopi.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.mappers.AgendamentoMapper;
import projetopi.projetopi.dto.request.AgendamentoCriacao;
import projetopi.projetopi.dto.response.AgendamentoConsulta;
import projetopi.projetopi.dto.response.HorarioDiaSemana;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.exception.AcessoNegadoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Dia;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


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
    private final BarbeiroServicoRepository barbeiroServicoRepository;
    private final UsuarioRepository usuarioRepository;

    private final FuncionarioService funcionarioService;


    private ModelMapper mapper;


    @Autowired
    private Global global;

    @Autowired
    private Token tk;

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


        List<AgendamentoConsulta> dto = new ArrayList<>();
        for (Agendamento a : agendamentos) {
            dto.add(AgendamentoMapper.toDto(a));
        }

        return dto;
    }


    public AgendamentoConsulta getOneAgendamento(String token, Integer id){

        if (!usuarioRepository.existsById(Integer.valueOf(tk.getUserIdByToken(token)))){
            throw new AcessoNegadoException("Usuário");
        }

        if (!repository.existsById(id)){
            throw new RecursoNaoEncontradoException("Agendamento", id);
        }

        return AgendamentoMapper.toDto(repository.findById(id).get());
    }

    public static void main(String[] args) {
        System.out.println(ChronoUnit.MINUTES.between(LocalTime.now(), LocalTime.now().plusMinutes(60)));
    }

    public List<HorarioDiaSemana> getHorarios(String token, BarbeiroServicoId barbeiroServicoId, LocalDate date){

        String dia3Letras = date.format(DateTimeFormatter.ofPattern("EEE", new Locale("pt")))
                                .substring(0, 3).toUpperCase();

        DiaSemana diaSemana = diaSemanaRepository.findByNomeAndBarbeariaId(Dia.valueOf(dia3Letras), barbeiroServicoId.getBarbearia());

        Servico servico = servicoRepository.findById(barbeiroServicoId.getServico()).get();

        Integer tempoEstimado = 60;

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
/*
        if (!usuarioRepository.existsById(Integer.valueOf(tk.getUserIdByToken(token)))){
            throw new AcessoNegadoException("Usuário");
        }
        List<Agendamento> agendamentos = repository.findAllByBarbeiroAndDate(barbeiroServicoId.getBarbeiro(), date);

        if (agendamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento encontrado");
        }

        List<AgendamentoConsulta> dto = new ArrayList<>();

        for (Agendamento a : agendamentos) {
            dto.add(AgendamentoMapper.toDto(a));
        }

        return dto;*/
    }


    public AgendamentoConsulta updateStatus(String token, Integer id, String status){

        if (!repository.existsById(id)){
            throw new RecursoNaoEncontradoException("Agendamento", id);
        }

        if (!usuarioRepository.existsById(Integer.valueOf(tk.getUserIdByToken(token)))){
            throw new AcessoNegadoException("Usuário");
        }

        Agendamento agendamento = repository.findById(id).get();


        if (usuarioRepository.findById(Integer.valueOf(tk.getUserIdByToken(token))).get().getDtype().equalsIgnoreCase("Barbeiro")){
            global.validaBarbearia(token);
            if (!status.equalsIgnoreCase("Concluido")
                    && !status.equalsIgnoreCase("Agendado")
                    && !status.equalsIgnoreCase("Cancelado")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
            }


        }else {
            if (!status.equalsIgnoreCase("Cancelado")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
            }
        }


        if (!(agendamento.getStatus().equalsIgnoreCase("Pendente")) &&
                !(agendamento.getStatus().equalsIgnoreCase("Agendado"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        if (agendamento.getStatus().equalsIgnoreCase("Pedente")
                && definirStatus(status).equalsIgnoreCase("Concluido")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        if (agendamento.getStatus().equalsIgnoreCase(definirStatus(status))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de agendamento inválido");
        }

        if(definirStatus(status).equalsIgnoreCase("Concluido")){
            agendamento.setDataHoraConcluido(LocalDateTime.now());
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



}
