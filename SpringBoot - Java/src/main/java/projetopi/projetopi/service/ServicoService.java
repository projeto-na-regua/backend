package projetopi.projetopi.service;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.mappers.ServicoMapper;
import projetopi.projetopi.dto.request.ServicoCriacao;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Token;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;


    @Autowired
    private BarbeariasRepository barbeariasRepository;


    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    public Token tk;

    @Autowired
    public ModelMapper mapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BarbeiroServicoRepository barbeiroServicoRepository;


    public List<ServicoConsulta> getAllServicos(String token){
        validarBarbearia(token);
        List<ServicoConsulta> servicos = new ArrayList<>();

        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return servicos;
    }

    public ServicoConsulta getServico(String token, Integer idServico){
        validarBarbearia(token);
        validarServico(idServico);
        ServicoConsulta servico = new ServicoConsulta(servicoRepository.findByBarbeariaIdAndId(getIdBarbearia(token), idServico));
        return servico;
    }


    public ServicoConsulta criar(String token, ServicoCriacao nvServico){
        validarBarbearia(token);

        Integer id = getIdBarbearia(token);
        Servico servico = ServicoMapper.toEntity(nvServico);
        servico.setBarbearia(barbeariasRepository.findById(id).get());
        Integer servicoId = servicoRepository.save(servico).getId();

        for (int i = 0; i < nvServico.getBarbeirosIds().size(); i++) {
            relacionarSericoBarbeiro(token, nvServico.getBarbeirosIds().get(i), servicoId);
        }
        return new ServicoConsulta(servicoRepository.findById(servicoId).get());
    }


    public ServicoConsulta atualizar(String token, Integer idServico, ServicoCriacao nvServico){
        validarBarbearia(token);
        validarServico(idServico);

        Servico servico = ServicoMapper.toEntity(nvServico);
        servico.setBarbearia(barbeariasRepository.getReferenceById(getIdBarbearia(token)));
        servico.setId(idServico);
        return new ServicoConsulta(servicoRepository.save(servico));
    }

    public void deletar(String token, Integer idServico){
        validarBarbearia(token);
        validarServico(idServico);
        servicoRepository.deleteById(idServico);
    }

    public Integer getIdBarbearia(String token){
        return getBarbeariaByToken(token).getId();
    }

    public BarbeiroServico relacionarSericoBarbeiro(String token, Integer barberiroId, Integer servicoId){
        Barbeiro barbeiro = barbeiroRepository.findById(barberiroId).get();
        Servico servico = servicoRepository.findById(servicoId).get();
        Barbearia barbearia = barbeariasRepository.findById(getIdBarbearia(token)).get();
        BarbeiroServico barbeiroServico = new BarbeiroServico();
        barbeiroServico.setBarbeiro(barbeiro);
        barbeiroServico.setServico(servico);
        barbeiroServico.setBarbearia(barbearia);
        return barbeiroServicoRepository.save(barbeiroServico);
    }

    void validarBarbearia(String token){
        if (!barbeariasRepository.existsById(getIdBarbearia(token))) {
            throw new RecursoNaoEncontradoException("Barbearia", token);
        }
    }

    void validarServico(Integer id){
        if (!servicoRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Serviço", id);
        }
    }

    void validaBarbeiro(Integer id){
        if (!barbeiroRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Barbeiro", id);
        }
    }

    public Usuario getBarbeiroByToken(String token){
        Integer id  = Integer.valueOf(tk.getUserIdByToken(token));
        return usuarioRepository.findById(id).get();
    }

    public Barbearia getBarbeariaByToken(String token){
        Barbeiro b = (Barbeiro) getBarbeiroByToken(token);
        return b.getBarbearia();
    }

}
