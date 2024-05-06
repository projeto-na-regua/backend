package projetopi.projetopi.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.util.Token;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class UsuarioService {


    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public Token token;

    @Autowired
    public ModelMapper mapper;

    // CADASTRO CLIENTE
    public String cadastrarCliente(CadastroCliente c){

        Integer idEdereco = enderecoRepository.save(c.gerarEndereco()).getId();
        Cliente cliente = c.gerarCliente();


        cliente.setEndereco(enderecoRepository.getReferenceById(idEdereco));
        clienteRepository.save(cliente);
        return token.getToken(cliente);
    }

    // CADASTRO BARBEIRO
    public String cadastrarBarbeiro(CadastroBarbearia nvBarbearia){

        Endereco endereco = nvBarbearia.gerarEndereco();
        Barbearia barbearia = nvBarbearia.gerarBarbearia();
        Barbeiro barbeiro = nvBarbearia.gerarBarbeiro();
        DiaSemana[] diaSemana = nvBarbearia.gerarSemena();

        Integer idEndereco = enderecoRepository.save(endereco).getId();

        barbearia.setEndereco(enderecoRepository.getReferenceById(idEndereco));

        Integer idBarbearia = barbeariasRepository.save(barbearia).getId();

        barbeiro.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));

        barbeiroRepository.save(barbeiro);

        for (DiaSemana d: diaSemana){
            d.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));
            diaSemanaRepository.save(d);
        }

        return token.getToken(barbeiro);
    }


    public UsuarioConsulta editarUsuario(Integer id, UsuarioConsulta nvUsuario){


        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            Cliente usuario = mapper.map(nvUsuario, Cliente.class);
            usuario.setId(clienteRepository.getReferenceById(id).getId());
            usuario.setSenha(usuarioRepository.getReferenceById(id).getSenha());
            usuarioRepository.save(usuario);
            return nvUsuario;

        }else{
            Barbeiro usuario = mapper.map(nvUsuario, Barbeiro.class);
            usuario.setId(usuarioRepository.getReferenceById(id).getId());
            usuario.setSenha(usuarioRepository.getReferenceById(id).getSenha());
            usuarioRepository.save(usuario);
            return nvUsuario;
        }
    }

    public Integer getUserId(String t){
       return Integer.valueOf(token.getUserIdByToken(t));
    }

    public boolean usuarioExistsById(Integer id){
        return usuarioRepository.existsById(id);
    }

    public boolean usuarioExistsByEmail(String email){
        return usuarioRepository.findByEmail(email) != null;
    }


    public String loginIsValid(LoginUsuario user){

        Usuario u = usuarioRepository.findByEmailAndSenha(user.getEmail(), user.getSenha());

        if (u != null){
            return token.getToken(u);
        }

        return null;
    }


    public List<UsuarioConsulta> getUsuario(String t){

        Integer id = Integer.valueOf(token.getUserIdByToken(t));

        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            return clienteRepository.findByInfoUsuario(id);

        }else{
            return barbeiroRepository.findByInfoUsuario(id);
        }

    }


}
