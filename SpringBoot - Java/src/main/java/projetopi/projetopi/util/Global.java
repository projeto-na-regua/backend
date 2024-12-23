package projetopi.projetopi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.AcessoNegadoException;
import projetopi.projetopi.exception.ConflitoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;

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
        Barbeiro barbeiro = (Barbeiro) getUsuarioByToken(token);
        if (!barbeiro.isAdm() || barbeiro.getBarbearia() == null){
            throw new AcessoNegadoException(recurso);
        }
    }

    public void validaBarbeiro(String token, String recurso){
        Integer id = getUsuarioByToken(token).getId();
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

    public Usuario getUsuarioByToken(String token){
        Integer id  = Integer.valueOf(tk.getUserIdByToken(token));
        return usuarioRepository.findById(id).get();
    }

    public Barbearia getBarbeariaByToken(String token){
        Barbeiro b = (Barbeiro) getUsuarioByToken(token);
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

    public void validarUpdate(UsuarioConsulta nvUsuario, Usuario usuario){
        if(!nvUsuario.getEmail().equals(usuario.getEmail())){
            if (usuarioRepository.findByEmail(nvUsuario.getEmail()) != null){
                throw new ConflitoException("Usuário", nvUsuario.getEmail());
            }
        }

        if(!nvUsuario.getUsername().equals(usuario.getUsername())){
            if (usuarioRepository.findByUsername(nvUsuario.getUsername()) != null){
                throw new ConflitoException("Usuário", nvUsuario.getUsername());
            }
        }
    }



    public void validarUsername(String username){
        if (usuarioRepository.findByUsername(username) != null){
            throw new ConflitoException("Usuário", username);
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

    public boolean validarSeUsuarioPossuiBarbearia(Integer id) {
        return barbeiroRepository.findById(id).isPresent();
    }


    public void validarUsuarioExiste(String token){
        if (!usuarioRepository.existsById(Integer.valueOf(tk.getUserIdByToken(token)))){
            throw new RecursoNaoEncontradoException("Usuário", token);
        }
    }

    public void validarBarbeariaExisteById(Integer id){
        if (!barbeariasRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Barbearia", id);
        }
    }

    public void validarEnderecoCadastrado(Endereco endereco){
        if (endereco == null || endereco.getId() == null) {
            throw new RecursoNaoEncontradoException("Endereco", endereco);
        }
    }


}
