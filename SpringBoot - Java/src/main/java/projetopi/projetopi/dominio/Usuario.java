package projetopi.projetopi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario", unique = true, nullable = false)
    private Integer id;
    @Column(name="nome", nullable = false)
    private String nome;
    @Column(name="email")
    private String email;
    @Column(name="senha")
    private String senha;
    @Column(name="celular")
    private String celular;
    @JsonIgnore
    @Column(name="imgPerfil")
    private byte[] imgPerfil;
    @Column(name="user_admin")
    private boolean adm;
    @Column(name="user_fk_barbearia")
    private Barbearia barbearia;

    public Usuario(){}

    public Usuario(Integer id, String nome, String email, String senha, String celular, byte[] imgPerfil, boolean adm, Barbearia barbearia) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.imgPerfil = imgPerfil;
        this.adm = adm;
        this.barbearia = barbearia;
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

    public boolean isAdm() {
        return adm;
    }

    public void setAdm(boolean adm) {
        this.adm = adm;
    }

    public Barbearia getBarbearia() {
        return barbearia;
    }

    public void setBarbearia(Barbearia barbearia) {
        this.barbearia = barbearia;
    }
}
