package projetopi.projetopi.dto.response;


import lombok.Getter;

import lombok.Setter;
import projetopi.projetopi.entity.Barbearia;

@Getter
@Setter
public class BarbeariaPesquisa {

    private Integer id;

    private String nomeNegocio;

    private String imgPerfil;

    private String logradouro;

    private Integer numero;

    private Double distancia;

    public BarbeariaPesquisa(Barbearia barbearia, Double distancia) {
        this.id = barbearia.getId();
        this.nomeNegocio = barbearia.getNomeNegocio();
        this.logradouro = barbearia.getEndereco().getLogradouro();
        this.numero = barbearia.getEndereco().getNumero();
        this.distancia = distancia;
        this.imgPerfil = barbearia.getImgPerfil();
    }
}
