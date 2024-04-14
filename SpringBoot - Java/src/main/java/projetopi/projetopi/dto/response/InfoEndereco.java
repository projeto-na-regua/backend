package projetopi.projetopi.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Cliente;

public class InfoEndereco {

    @Size(max = 9)
    private String cep;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String cidade;

    private String estado;

    public InfoEndereco() {
    }

    public InfoEndereco(Barbearia barbearia) {
        this.cep = barbearia.getEndereco().getCep();
        this.logradouro = barbearia.getEndereco().getLogradouro();
        this.numero = barbearia.getEndereco().getNumero();
        this.complemento = barbearia.getEndereco().getComplemento();
        this.cidade = barbearia.getEndereco().getCidade();
        this.estado = barbearia.getEndereco().getEstado();
    }

    public InfoEndereco(Cliente cliente) {
        this.cep = cliente.getEndereco().getCep();
        this.logradouro = cliente.getEndereco().getLogradouro();
        this.numero = cliente.getEndereco().getNumero();
        this.complemento = cliente.getEndereco().getComplemento();
        this.cidade = cliente.getEndereco().getCidade();
        this.estado = cliente.getEndereco().getEstado();
    }


    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}

