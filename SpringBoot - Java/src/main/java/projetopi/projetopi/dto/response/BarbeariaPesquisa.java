package projetopi.projetopi.dto.response;


import lombok.Getter;

import lombok.Setter;
import projetopi.projetopi.entity.Barbearia;

import java.math.BigDecimal;

@Getter
@Setter
public class BarbeariaPesquisa {

    private Integer id;

    private String nomeNegocio;

    private String imgPerfil;

    private String logradouro;

    private Integer numero;

    private BigDecimal mediaAvaliacao;



    public BarbeariaPesquisa(Barbearia barbearia, BigDecimal mediaAvaliacao, String linkImgPerfil) {
        this.id = barbearia.getId();
        this.nomeNegocio = barbearia.getNomeNegocio();
        this.logradouro = barbearia.getEndereco().getLogradouro();
        this.numero = barbearia.getEndereco().getNumero();
        this.imgPerfil = linkImgPerfil;
        this.mediaAvaliacao = mediaAvaliacao;
    }

    public BarbeariaPesquisa(Barbearia barbearia, String linkImgPerfil) {
        this.id = barbearia.getId();
        this.nomeNegocio = barbearia.getNomeNegocio();
        this.logradouro = barbearia.getEndereco().getLogradouro();
        this.numero = barbearia.getEndereco().getNumero();
        this.imgPerfil = linkImgPerfil;
    }

    public BarbeariaPesquisa(Barbearia barbearia) {
        this.id = barbearia.getId();
        this.nomeNegocio = barbearia.getNomeNegocio();
        this.logradouro = barbearia.getEndereco().getLogradouro();
        this.numero = barbearia.getEndereco().getNumero();
        this.imgPerfil = barbearia.getImgPerfil();
    }

    public BarbeariaPesquisa() {
    }
}
