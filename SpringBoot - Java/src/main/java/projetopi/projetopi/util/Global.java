package projetopi.projetopi.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.geolatte.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import projetopi.projetopi.controller.UsuarioController;
import projetopi.projetopi.dto.response.Coordenada;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.AcessoNegadoException;
import projetopi.projetopi.exception.ConflitoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;

import javax.imageio.spi.ServiceRegistry;
import java.util.List;


@Component
public class Global {

    @Autowired
    private  BarbeariasRepository barbeariasRepository;
    @Autowired
    private  UsuarioRepository usuarioRepository;
    @Autowired
    private  BarbeiroRepository barbeiroRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private Token tk;


    public void validarBarbeiroAdm(String token, String recurso){
        Barbeiro barbeiro = (Barbeiro) getBarbeiroByToken(token);
        if (!barbeiro.isAdm() || barbeiro.getBarbearia() == null){
            throw new AcessoNegadoException(recurso);
        }
    }

    public void validaBarbeiro(String token, String recurso){
        Integer id = getBarbeiroByToken(token).getId();
        if (!barbeiroRepository.existsById(id)){
            throw new AcessoNegadoException(recurso);
        }
    }
    public void validaCliente(String token, String recurso){
        Integer id = Integer.valueOf(tk.getUserIdByToken(token));
        if (!clienteRepository.existsById(id)){
            throw new AcessoNegadoException(recurso);
        }
    }


    public void validaBarbearia(String token){
        Integer id = getBarbeariaByToken(token).getId();
        if (!barbeariasRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Barbearia", id);
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


    public boolean isCliente(String token){
        return  usuarioRepository.findById(Integer.valueOf(tk.getUserIdByToken(token))).get().getDtype().equalsIgnoreCase("Cliente");
    }

    public static boolean isBarbeiro(Usuario user){
        return user.getDtype().equalsIgnoreCase("Barbeiro");
    }


    public void validarEmail(String email){
        if (usuarioRepository.findByEmail(email) != null){
            throw new ConflitoException("Usuário", email);
        }
    }

    public void validarToken(String token){
        if (token == null) throw new RecursoNaoEncontradoException("Usuário", token);
    }


    public void validarCpf(String cpf){
        if (barbeariasRepository.findByCpf(cpf) != null){
            throw new ConflitoException("Usuário", cpf);
        }
    }


    public void validarSeUsuarioPossuiBarbearia(String token){
        Integer id = Integer.valueOf(tk.getUserIdByToken(token));
        Usuario u = usuarioRepository.findById(id).get();
        List<Barbeiro> barbeiros = barbeiroRepository.findAll();

        for (Barbeiro b: barbeiros){
            if(b.getId() == u.getId()){
                if(b.getBarbearia() != null){
                    throw new ConflitoException("Barbearia", u);
                }
            }
        }
    }

    public void validarUsuarioExiste(String token){
        if (!usuarioRepository.existsById(Integer.valueOf(tk.getUserIdByToken(token)))){
            throw new RecursoNaoEncontradoException("Usuário", token);
        }
    }

    public void validarEnderecoCadastrado(Endereco endereco){
        if (endereco == null || endereco.getId() == null) {
            throw new RecursoNaoEncontradoException("Endereco", endereco);
        }
    }


}
