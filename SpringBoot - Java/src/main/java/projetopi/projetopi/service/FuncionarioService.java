package projetopi.projetopi.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dto.request.BarbeiroCriacao;

import projetopi.projetopi.dto.response.BarbeiroConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.relatorios.RelatorioBarbeiro;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.util.Token;

import java.util.List;

import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.status;

@Service
public class FuncionarioService {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    public Token tk;

    @Autowired
    public ModelMapper mapper;

    public boolean isCliente(String token){
        return usuarioRepository.findById(Integer.valueOf(tk.getUserIdByToken(token))).get().getDtype().equals("Cliente");
    }

    public Barbeiro getBarbeiroByToken(String token){
        Integer id  = Integer.valueOf(tk.getUserIdByToken(token));
        return barbeiroRepository.findById(id).get();
    }

    public Barbearia getBarbeariaByToken(String token){
        Barbeiro b = getBarbeiroByToken(token);
        return b.getBarbearia();
    }

    public boolean barbeariaExiste(String token){
        return barbeariasRepository.existsById(getBarbeariaByToken(token).getId());
    }

    public boolean validarPermissioes(String token){
        if(isCliente(token)){
            return false;
        }
        if (getBarbeariaByToken(token) == null){
            return false;
        }
        return getBarbeiroByToken(token).isAdm();
    }

    public boolean userComumExiste(String email){
        return clienteRepository.findByEmail(email) == null;
    }

    public List<BarbeiroConsulta> getFuncionarios(String token){
        List<Barbeiro> barbeiros = barbeiroRepository.findByBarbeariaIdAndUsuarioIdNot(getBarbeariaByToken(token).getId(), getBarbeiroByToken(token).getId());
        return mapper.map(barbeiros, new TypeToken<List<BarbeiroConsulta>>(){}.getType());
    }

    public BarbeiroConsulta getFuncionarioEspecifico(String email){;
        return mapper.map(barbeiroRepository.findByEmail(email), BarbeiroConsulta.class);
    }


    public BarbeiroConsulta criarBarbeiro(String token, BarbeiroCriacao nvBarbeiro){
        Barbeiro b = mapper.map(nvBarbeiro, Barbeiro.class);
        b.setBarbearia(getBarbeariaByToken(token));
        barbeiroRepository.save(b);
        return mapper.map(b, BarbeiroConsulta.class);
    }


    public void adicionarBarberiro(String token, String email){
        clienteRepository.atualizarClienteParaBarbeiro(clienteRepository.findByEmail(email).getId(), getBarbeariaByToken(token), false);

    }

    public UsuarioConsulta buscarEmailBarbeiro(String email){
        return mapper.map(clienteRepository.findByEmail(email), UsuarioConsulta.class);
    }

    public boolean barbeiroExiste(String email){
        return barbeiroRepository.findByEmail(email) != null;
    }


    public void deleteBarbeiro(String email){
        barbeiroRepository.atualizarBarbeiroParaCliente(barbeiroRepository.findByEmail(email).getId());
    }


    public void gerarRelatorioBarbeiro(String token, List<Barbeiro> barbeiros){
        RelatorioBarbeiro.gravarRelatorioFinanceiro(barbeiros,"relatório_barbeiros");
    }
}
