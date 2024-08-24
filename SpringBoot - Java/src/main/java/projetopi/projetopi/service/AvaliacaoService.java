package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.AvaliacaoConsulta;
import projetopi.projetopi.dto.response.TotalValorPorDia;
import projetopi.projetopi.entity.Agendamento;
import projetopi.projetopi.entity.Avaliacao;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.AgendaRepository;
import projetopi.projetopi.repository.AvaliacaoRepository;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.util.Global;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AgendaRepository repositoryAgenda;

    @Autowired
    private AvaliacaoRepository repository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private Global global;


    public List<AvaliacaoConsulta> getAvaliacoes(String token, Integer quantidade) {

        global.validarBarbeiroAdm(token, "Barbeiro");
        global.validaBarbearia(token);
        Barbearia barbearia = global.getBarbeariaByToken(token);


        List<AvaliacaoConsulta> dto = repositoryAgenda.findUltimasAvaliacoes(barbearia.getId(), quantidade);

        if (dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento concluído encontrado");
        }

        return dto;
    }

    public List<AvaliacaoConsulta> getAvaliacoesClienteSide(String token, Integer quantidade, Integer idBarbearia) {

        global.validaCliente(token, "Cliente");

        Barbearia barbearia = barbeariasRepository.findById(idBarbearia)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Barbearia", idBarbearia));

        List<AvaliacaoConsulta> dto = repositoryAgenda.findUltimasAvaliacoes(barbearia.getId(), quantidade);

        if (dto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento concluído encontrado");
        }

        return dto;
    }



    public Avaliacao postAvaliacao(String token, Avaliacao a, Integer idAgendamento){
        global.validaCliente(token, "Cliente");


        Avaliacao avaliacaoSalva = repository.save(a);

        if (!repositoryAgenda.existsById(idAgendamento)) throw new  ResponseStatusException(HttpStatus.BAD_REQUEST);

        Agendamento agendamento = repositoryAgenda.findById(idAgendamento).get();


        agendamento.setAvaliacao(avaliacaoSalva);
        repositoryAgenda.save(agendamento);
        return avaliacaoSalva;
    }
}
