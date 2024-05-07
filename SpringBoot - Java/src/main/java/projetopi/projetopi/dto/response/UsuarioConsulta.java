package projetopi.projetopi.dto.response;

import lombok.Getter;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Cliente;


@Getter
public class UsuarioConsulta {

    private String nome;

    private String email;

    private String celular;

    private String imgPerfil;


}
