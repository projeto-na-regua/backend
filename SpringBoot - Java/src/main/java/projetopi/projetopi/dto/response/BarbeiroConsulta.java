package projetopi.projetopi.dto.response;

import lombok.Getter;
import projetopi.projetopi.entity.Barbeiro;

@Getter
public class BarbeiroConsulta {

    private Integer id;

    private String nome;

    private String email;

    private String imgPerfil;

    private String especialidade;

    public BarbeiroConsulta() {}

    public BarbeiroConsulta(Barbeiro barbeiro) {
        this.id = barbeiro.getId();
        this.nome = barbeiro.getNome();
        this.email = barbeiro.getEmail();
        this.imgPerfil = barbeiro.getImgPerfil();
        this.especialidade = barbeiro.getEspecialidade() == null ? null : barbeiro.getEspecialidade().getNome();
    }

    public BarbeiroConsulta(Barbeiro barbeiro, String linkImg) {
        this.id = barbeiro.getId();
        this.nome = barbeiro.getNome();
        this.email = barbeiro.getEmail();
        this.imgPerfil = linkImg;
        this.especialidade = barbeiro.getEspecialidade() == null ? null : barbeiro.getEspecialidade().getNome();
    }
}
