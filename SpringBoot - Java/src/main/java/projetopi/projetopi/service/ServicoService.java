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
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Servico;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.BarbeiroRepository;
import projetopi.projetopi.repository.ServicoRepository;
import projetopi.projetopi.repository.UsuarioRepository;
import projetopi.projetopi.util.Token;

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


    public List<ServicoConsulta> getAllServicos(String token){
        validarBarbearia(token);
        List<ServicoConsulta> servicos = servicoRepository.findByInfoServicoBarbearia(getIdBarbearia(token));

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
        validaBarbeiro(nvServico.getBarbeiro());

        Servico servico = ServicoMapper.toEntity(nvServico);
        servico.setBarbearia(barbeariasRepository.getReferenceById(getIdBarbearia(token)));
        return new ServicoConsulta(servicoRepository.save(servico));
    }


    public ServicoConsulta atualizar(String token, Integer idServico, ServicoCriacao nvServico){
        validarBarbearia(token);
        validaBarbeiro(nvServico.getBarbeiro());
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
