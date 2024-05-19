package projetopi.projetopi.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.Endereco;

@Getter
public class CadastroCliente {

    @NotBlank
    @Size(min = 5, max = 120)
    private String nome;

    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    @Size(min = 8)
    @NotBlank
    private String senha;

    @NotBlank
    @Size(max = 15)
    private String celular;

    @Size(max = 9)
    private String cep;
    private String logradouro;
    private Integer numero;

    private String complemento;

    private String cidade;

    private String estado;


    public CadastroCliente() {
    }

    public CadastroCliente(String nome, String email, String senha, String celular, String cep, String logradouro, Integer numero, String complemento, String cidade, String estado) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
    }

}
