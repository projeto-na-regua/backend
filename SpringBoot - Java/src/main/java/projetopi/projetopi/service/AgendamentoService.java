package projetopi.projetopi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.mappers.AgendamentoMapper;
import projetopi.projetopi.dto.request.AgendamentoCriacao;
import projetopi.projetopi.dto.response.AgendamentoConsulta;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;

import java.util.ArrayList;
import java.util.List;


@Service
public class AgendamentoService {

    @Autowired
    private AgendaRepository repository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private BarbeiroServicoRepository barbeiroServicoRepository;

    private ModelMapper mapper;

    public List<AgendamentoConsulta> getAgendamento(){
        var lista = repository.findAll();
        List<AgendamentoConsulta> agendamentos = new ArrayList<>();
        for (Agendamento a : lista){
            agendamentos.add(AgendamentoMapper.toDto(a));
        }

        if (agendamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }
        return agendamentos;
    }


//    public List<Barbeiro> getBarbeirosByServico(Integer idServico){
//        return barbeiroRepository.findByServico(idServico);
//    }



    public AgendamentoConsulta adicionarAgendamento(AgendamentoCriacao a){

        if (!servicoRepository.existsById(a.getIdServico())){
            throw new RecursoNaoEncontradoException("Serviço", a.getIdServico());
        }

        if (!barbeiroRepository.existsById(a.getIdBarbeiro())){
            throw new RecursoNaoEncontradoException("Barbeiro", a.getIdBarbeiro());
        }

        if (!clienteRepository.existsById(a.getIdCliente())){
            throw new RecursoNaoEncontradoException("Cliente", a.getIdCliente());
        }

        if (!barbeariasRepository.existsById(a.getIdBarbearia())){
            throw new RecursoNaoEncontradoException("Barbearia", a.getIdBarbearia());
        }
        Servico servico = servicoRepository.findById(a.getIdServico()).get();
        Barbeiro barbeiro = barbeiroRepository.findById(a.getIdBarbeiro()).get();
        Cliente cliente = clienteRepository.findById(a.getIdCliente()).get();
        Barbearia barbearia = barbeariasRepository.findById(a.getIdBarbearia()).get();
        Agendamento nvAgendamento = new Agendamento(a.getDataHora(), servico, barbeiro, cliente, barbearia);
        return AgendamentoMapper.toDto(repository.save(nvAgendamento));
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<Agendamento> atualizarAgendamento(@RequestBody Agendamento a,
//                                                            @PathVariable Integer id){
//        if (repository.existsById(id)) {
//            a.setId(id);
//            repository.save(a);
//            return status(200).body(a);
//        }
//        return status(404).build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Agendamento> deletarAgendamento(@PathVariable Integer id){
//        if (repository.existsById(id)) {
//            repository.deleteById(id);
//            return status(204).build();
//        }
//        return status(404).build();
//    }

}
