package projetopi.projetopi.dto.response;

import jakarta.validation.constraints.Size;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.Endereco;

public class EnderecoConsulta {

    @Size(max = 9)
    private String cep;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String cidade;

    private String estado;

    public EnderecoConsulta() {
    }

    public EnderecoConsulta(Barbearia barbearia) {
        this.cep = barbearia.getEndereco().getCep();
        this.logradouro = barbearia.getEndereco().getLogradouro();
        this.numero = barbearia.getEndereco().getNumero();
        this.complemento = barbearia.getEndereco().getComplemento();
        this.cidade = barbearia.getEndereco().getCidade();
        this.estado = barbearia.getEndereco().getEstado();
    }

    public EnderecoConsulta(Endereco barbearia) {
        this.cep = barbearia.getCep();
        this.logradouro = barbearia.getLogradouro();
        this.numero = barbearia.getNumero();
        this.complemento = barbearia.getComplemento();
        this.cidade = barbearia.getCidade();
        this.estado = barbearia.getEstado();
    }


    public EnderecoConsulta(Cliente cliente) {
        this.cep = cliente.getEndereco().getCep();
        this.logradouro = cliente.getEndereco().getLogradouro();
        this.numero = cliente.getEndereco().getNumero();
        this.complemento = cliente.getEndereco().getComplemento();
        this.cidade = cliente.getEndereco().getCidade();
        this.estado = cliente.getEndereco().getEstado();
    }

    public Endereco gerarEndereco(){
        return new Endereco(cep, logradouro, numero, complemento, cidade, estado);
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

