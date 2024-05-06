package projetopi.projetopi.dto.response;

import lombok.Getter;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Especialidade;

@Getter
public class BarbeiroConsulta {

    private String nome;

    private String email;

    private byte[] imgPerfil;

    private String especialidade;

    public BarbeiroConsulta() {}
}
