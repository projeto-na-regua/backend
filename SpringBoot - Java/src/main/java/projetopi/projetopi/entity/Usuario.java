package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @Size(min = 8)
    @NotBlank
    @Column(name="senha")
    private String senha;

    @NotBlank
    @Size(max = 15)
    @Column(name="celular")
    private String celular;

    @JsonIgnore
    @Column(name="img_perfil")
    private String imgPerfil;

    @JsonIgnore
    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;

    @OneToOne
    @JoinColumn(name = "usuario_fk_endereco", nullable = true)
    private Endereco endereco;

    public Usuario(String nome, String email, String celular) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
    }

    public Usuario(String nome, String email, String senha, String celular, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.endereco = endereco;
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(String nome) {
        this.nome = nome;
    }

    public Usuario(){}

    public Usuario(Integer id, String nome, String email, String senha, String celular, String imgPerfil, String dtype, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.imgPerfil = imgPerfil;
        this.dtype = dtype;
        this.endereco = endereco;
    }
}
