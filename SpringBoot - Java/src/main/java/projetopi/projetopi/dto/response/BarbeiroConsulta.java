package projetopi.projetopi.dto.response;

import lombok.Getter;
import projetopi.projetopi.entity.Barbeiro;

@Getter
public class BarbeiroConsulta {

    private String nome;

    private String email;

    private String imgPerfil;

    private String especialidade;

    public BarbeiroConsulta() {}

    public BarbeiroConsulta(Barbeiro barbeiro) {
        this.nome = barbeiro.getNome();
        this.email = barbeiro.getEmail();
        this.imgPerfil = barbeiro.getImgPerfil();
        this.especialidade = barbeiro.getEspecialidade() == null ? null : barbeiro.getEspecialidade().getNome();
    }
}
