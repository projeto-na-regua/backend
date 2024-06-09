package projetopi.projetopi.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.AcessoNegadoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.BarbeiroRepository;
import projetopi.projetopi.repository.ServicoRepository;
import projetopi.projetopi.repository.UsuarioRepository;

import javax.imageio.spi.ServiceRegistry;


@Component
public class Global {

    @Autowired
    private BarbeariasRepository barbeariasRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BarbeiroRepository barbeiroRepository;
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

    public  void validaBarbearia(String token){
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
}
