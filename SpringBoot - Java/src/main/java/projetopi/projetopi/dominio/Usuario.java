package projetopi.projetopi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario", nullable = false)
    private Integer id;

    @NotBlank
    @Size(min = 5, max = 120)
    @Column(name="nome", nullable = false)
    private String nome;

    @Email
    @NotBlank
    @Size(min = 0, max = 250)
    @Column(name="email")
    private String email;

    @Size(min = 8, max = 12)
    @NotBlank
    @Column(name="senha")
    private String senha;

    @NotBlank
    @Size(max = 15)
    @Column(name="celular")
    private String celular;

    @JsonIgnore
    @Column(name="img_perfil")
    private byte[] imgPerfil;

    @JsonIgnore
    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;


    public Usuario(String nome, String email, String celular) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
    }



    public Usuario(String nome) {
        this.nome = nome;
    }

    public Usuario(){}

    public Usuario(Integer id, String nome, String email, String senha, String celular, byte[] imgPerfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.imgPerfil = imgPerfil;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public byte[] getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(byte[] imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }
}
