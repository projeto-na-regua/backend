package projetopi.projetopi.service;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.mappers.ServicoMapper;
import projetopi.projetopi.dto.request.ServicoCriacao;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    private final BarbeariasRepository barbeariasRepository;

    private final BarbeiroRepository barbeiroRepository;

    public final Token tk;

    public final ModelMapper mapper;

    private final UsuarioRepository usuarioRepository;

    private final BarbeiroServicoRepository barbeiroServicoRepository;

    private final Global global;

    private final ServicoMapper servicoMapper;

    @Autowired
    public ServicoService(BarbeariasRepository barbeariasRepository, BarbeiroRepository barbeiroRepository, BarbeiroServicoRepository barbeiroServicoRepository, Global global, ModelMapper mapper, ServicoRepository servicoRepository, Token tk, UsuarioRepository usuarioRepository, ServicoMapper servicoMapper) {
        this.barbeariasRepository = barbeariasRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.barbeiroServicoRepository = barbeiroServicoRepository;
        this.global = global;
        this.mapper = mapper;
        this.servicoRepository = servicoRepository;
        this.tk = tk;
        this.usuarioRepository = usuarioRepository;
        this.servicoMapper = servicoMapper;
    }

    public List<ServicoConsulta> getAllServicos(String token){
        validacoesPermissoes(token);
        List<Servico> servicos = servicoRepository.findByBarbeariaId(global.getBarbeariaByToken(token).getId());

        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return servicoMapper.toDto(servicos);
    }

    public List<ServicoConsulta> getAllServicosByStatus(String token, String status){
        validacoesPermissoes(token);
        List<Servico> servicos = servicoRepository.findByBarbeariaIdAndStatus(global.getBarbeariaByToken(token).getId(),
                                                                              status.equalsIgnoreCase("active"));

        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return servicoMapper.toDto(servicos);
    }

    public List<ServicoConsulta> getAllServicosByBarbeariaForClientes(String token, Integer idBarbearia){
        global.validaCliente(token, "Cliente");
        List<Servico> servicos = servicoRepository.findByBarbeariaIdAndStatus(idBarbearia, true);

        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return servicoMapper.toDto(servicos);
    }


    public ServicoConsulta getServico(String token, Integer idServico){
        validacoesPermissoes(token);
        validarServico(idServico); // Lança exceção se o serviço não for encontrado
        Barbearia barbearia = global.getBarbeariaByToken(token);
        Servico servico = servicoRepository.findByBarbeariaIdAndId(barbearia.getId(), idServico);
        if (servico == null) {
            throw new RecursoNaoEncontradoException("Serviço", idServico);
        }
        return ServicoMapper.toDto(servico);
    }



    public ServicoConsulta criar(String token, ServicoCriacao nvServico){
        validacoesPermissoes(token);

        Integer id = getIdBarbearia(token);
        Servico servico = ServicoMapper.toEntity(nvServico);

        servico.setBarbearia(barbeariasRepository.findById(id).get());
        servico.setStatus(true);
        Integer servicoId = servicoRepository.save(servico).getId();

        if (nvServico.getBarbeirosEmails() != null && !nvServico.getBarbeirosEmails().isEmpty()) {
            for (int i = 0; i < nvServico.getBarbeirosEmails().size(); i++) {
                relacionarSericoBarbeiro(token, nvServico.getBarbeirosEmails().get(i), servicoId);
            }
        }
        return new ServicoConsulta(servicoRepository.findById(servicoId).get());
    }


    public ServicoConsulta atualizar(String token, Integer idServico, ServicoCriacao nvServico){
        validacoesPermissoes(token);
        validarServico(idServico);

        Servico servico = ServicoMapper.toEntity(nvServico);
        servico.setBarbearia(barbeariasRepository.getReferenceById(getIdBarbearia(token)));
        servico.setId(idServico);
        servico.setStatus(servicoRepository.findById(idServico).get().getStatus());

        for (int i = 0; i < nvServico.getBarbeirosEmails().size(); i++) {
            relacionarSericoBarbeiro(token, nvServico.getBarbeirosEmails().get(i), idServico);
        }
        return new ServicoConsulta(servicoRepository.save(servico));
    }

    public ServicoConsulta updateStatus(String token, Integer idServico){
        validacoesPermissoes(token);
        validarServico(idServico);

        Servico servico = servicoRepository.findById(idServico).get();
        if (servico.getStatus()) {
            servico.setStatus(false);
        } else {
            servico.setStatus(true);
        }

        servico.setId(servico.getId());
        return new ServicoConsulta(servicoRepository.save(servico));
    }

    public void deletar(String token, Integer idServico){
        validacoesPermissoes(token);
        validarServico(idServico);
        servicoRepository.deleteById(idServico);
    }

    public void deletarRelacaoServicoBarbeiro(String token, Integer id, String email){
        validacoesPermissoes(token);
        validarServico(id);

        Barbearia barbearia = global.getBarbeariaByToken(token);
        Barbeiro barbeiro = barbeiroRepository.findByEmail(email);
        Servico servico = servicoRepository.findById(id).get();

        BarbeiroServicoId barbeiroServicoId = new BarbeiroServicoId(barbeiro.getId(), servico.getId(), barbearia.getId());
        if (!barbeiroServicoRepository.existsById(barbeiroServicoId)){
            throw  new RecursoNaoEncontradoException("Relação entre barbeiro e servico", barbeiroServicoId);
        }
        barbeiroServicoRepository.deleteById(barbeiroServicoId);
    }


    public List<ServicoConsulta> findByName(String token, String tipoServico){
        global.validaBarbeiro(token, "Barbeiro");
        global.validaBarbearia(token);
        List<Servico> servicos = servicoRepository.findByTipoServicoContaining(tipoServico);

        if(servicos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        List<ServicoConsulta> dto = new ArrayList<>();
        for (Servico s : servicos){
            dto.add(new ServicoConsulta(s));
        }

        return dto;
    }

    public Integer getIdBarbearia(String token){
        return global.getBarbeariaByToken(token).getId();
    }

    public BarbeiroServico relacionarSericoBarbeiro(String token, String emailBarbeiro, Integer servicoId){
        Barbeiro barbeiro = barbeiroRepository.findByEmail(emailBarbeiro);
        Servico servico = servicoRepository.findById(servicoId).get();
        Barbearia barbearia = barbeariasRepository.findById(getIdBarbearia(token)).get();
        BarbeiroServico barbeiroServico = new BarbeiroServico();
        barbeiroServico.setBarbeiro(barbeiro);
        barbeiroServico.setServico(servico);
        barbeiroServico.setBarbearia(barbearia);
        return barbeiroServicoRepository.save(barbeiroServico);
    }


    void validarServico(Integer id){
        if (!servicoRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Serviço", id);
        }
    }


    void validacoesPermissoes(String token){
        global.validaBarbeiro(token, "Servico");
        global.validarBarbeiroAdm(token, "Servico");
        global.validaBarbearia(token);
    }

}
