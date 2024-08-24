package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Usuario;

@Getter
@Setter
public class UsuarioConsulta {

    private String nome;

    private String email;

    private String celular;


    public UsuarioConsulta(Usuario user) {
        this.nome = user.getNome();
        this.email = user.getNome();
        this.celular = user.getCelular();
    }

    public UsuarioConsulta() {
    }
}
