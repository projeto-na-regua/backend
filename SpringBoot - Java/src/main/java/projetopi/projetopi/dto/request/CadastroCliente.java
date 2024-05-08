package projetopi.projetopi.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import projetopi.projetopi.dominio.Cliente;
import projetopi.projetopi.dominio.Endereco;
import projetopi.projetopi.dominio.Usuario;

public class CadastroCliente {

    @NotBlank
    @Size(min = 5, max = 120)
    private String nome;

    @Email
    @NotBlank
    @Column(name="email")
    private String email;

    @Size(min = 8, max = 12)
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

    public Endereco gerarEndereco(){
        Endereco endereco = new Endereco();

        endereco.setCep(cep);
        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setComplemento(complemento);
        endereco.setEstado(estado);
        endereco.setCidade(cidade);

        return endereco;
    }

    public Cliente gerarUsuario(){

        Cliente cliente = new Cliente();

        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(senha);
        cliente.setCelular(celular);


        return cliente;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getCelular() {
        return celular;
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
