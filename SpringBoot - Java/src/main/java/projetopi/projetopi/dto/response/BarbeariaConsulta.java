package projetopi.projetopi.dto.response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.DiaSemana;


@Getter
@Setter
public class BarbeariaConsulta {

    private Integer id;
    private String imgPerfil;
    private String imgBanner;
    private String nomeNegocio;
    private String emailNegocio;
    private String celularNegocio;
    private String descricao;
    private String cep;
    private String logradouro;
    private Integer numero;
    private String complemento;
    private String cidade;
    private String estado;
    private DiaSemana[] diaSemanas;


    public BarbeariaConsulta() {
    }

    public BarbeariaConsulta(Barbearia barbearia, DiaSemana[] semana) {
        this.id = barbearia.getId();
        this.nomeNegocio = barbearia.getNomeNegocio();
        this.emailNegocio = barbearia.getEmailNegocio();
        this.celularNegocio = barbearia.getCelularNegocio();
        this.descricao = barbearia.getDescricao();
        this.cep = barbearia.getEndereco().getCep();
        this.logradouro = barbearia.getEndereco().getLogradouro();
        this.numero = barbearia.getEndereco().getNumero();
        this.complemento = barbearia.getEndereco().getComplemento();
        this.cidade = barbearia.getEndereco().getCidade();
        this.estado = barbearia.getEndereco().getEstado();
        this.diaSemanas = semana;
        this.imgPerfil = barbearia.getImgPerfil();
        this.imgBanner = barbearia.getImgBanner();
    }

}
