package projetopi.projetopi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import projetopi.projetopi.dto.response.BarbeariaConsulta;

@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco", nullable = false)
    private Integer id;

    @Size(max = 9)
    @Column(name = "cep")
    private String cep;
    @Column(name = "logradouro")
    private String logradouro;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "cidade")
    private String cidade;
    @Column(name = "estado")
    private String estado;

    public Endereco() {
    }

    public Endereco(String cep, String logradouro, Integer numero, String complemento, String cidade, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Endereco(BarbeariaConsulta barbeariaConsulta) {
        this.cep = barbeariaConsulta.getCep();
        this.logradouro = barbeariaConsulta.getLogradouro();
        this.numero = barbeariaConsulta.getNumero();
        this.complemento = barbeariaConsulta.getComplemento();
        this.cidade = barbeariaConsulta.getCidade();
        this.estado = barbeariaConsulta.getEstado();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}