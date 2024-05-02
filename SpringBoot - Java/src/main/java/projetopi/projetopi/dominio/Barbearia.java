package projetopi.projetopi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Barbearia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_barbearia", nullable = false)
    private Integer id;

    @Column(name="nome_negocio")
    private String nomeNegocio;

    @JsonIgnore
    @Column(name="img_perfil")
    private byte[] imgPerfil;

    @Column(name = "email_negocio", nullable = true)
    @Email
    private String emailNegocio;

    @Size(max = 15)
    @Column(name = "celular_negocio", nullable = true)
    private String celularNegocio;

    @Size(max = 18)
    @Column(name = "cnpj", nullable = true)
    private String cnpj;

    @Column(name = "cpf", nullable = true)
    @CPF
    private String cpf;

    @Column(name = "descricao", nullable = true)
    private String descricao;

    @OneToOne()
    @JoinColumn(name = "barbearia_fk_endereco", nullable = false)
    private Endereco endereco;

    public Barbearia(String nomeNegocio) {
        this.nomeNegocio = nomeNegocio;
    }

    public Barbearia() {
    }

    public Barbearia(Endereco endereco) {
        this.endereco = endereco;
    }

    public Barbearia(String nomeNegocio, String emailNegocio, String celularNegocio, String cnpj, String cpf, String descricao) {
        this.nomeNegocio = nomeNegocio;
        this.emailNegocio = emailNegocio;
        this.celularNegocio = celularNegocio;
        this.cnpj = cnpj;
        this.cpf = cpf;
        this.descricao = descricao;
    }

    public Barbearia(String nomeNegocio, String emailNegocio, String celularNegocio, String descricao) {
        this.nomeNegocio = nomeNegocio;
        this.emailNegocio = emailNegocio;
        this.celularNegocio = celularNegocio;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public byte[] getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(byte[] imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public String getEmailNegocio() {
        return emailNegocio;
    }

    public void setEmailNegocio(String emailNegocio) {
        this.emailNegocio = emailNegocio;
    }

    public String getCelularNegocio() {
        return celularNegocio;
    }

    public void setCelularNegocio(String celularNegocio) {
        this.celularNegocio = celularNegocio;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getNomeNegocio() {
        return nomeNegocio;
    }

    public void setNomeNegocio(String nomeNegocio) {
        this.nomeNegocio = nomeNegocio;
    }
}
