package projetopi.projetopi.dto.response;

import lombok.Getter;

@Getter
public class BarbeiroConsulta {

    private String nome;

    private String email;

    private byte[] imgPerfil;

    private String especialidade;

    public BarbeiroConsulta() {}
}
