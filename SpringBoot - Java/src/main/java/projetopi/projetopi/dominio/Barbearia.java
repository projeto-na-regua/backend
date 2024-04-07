package projetopi.projetopi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
public class Barbearia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_barbearia",unique = true, nullable = false)
    private Integer id;
    @Column(name="nome_negocio", unique = true, nullable = false)
    private String nomeDoNegocio;
    @Column(name="celular", unique = true, nullable = false)
    private String celular;
    @Column(name="email", unique = true, nullable = false)
    private String email;
    @JsonIgnore
    @Column(name="img_perfil")
    private byte[] imgPerfil;
    @OneToOne(cascade = CascadeType.ALL)
    //name: nome da coluna 'fk', referencedColumnName: nome da coluna original da tabela referenciada:
    @JoinColumn(name = "barbearia_fk_endereco", referencedColumnName = "id_endereco")
    private Endereco endereco;

    public Barbearia(String nomeDoNegocio, String celular, String email, byte[] imgPerfil, Endereco endereco) {
        this.nomeDoNegocio = nomeDoNegocio;
        this.celular = celular;
        this.email = email;
        this.imgPerfil = imgPerfil;
        this.endereco = endereco;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeDoNegocio() {
        return nomeDoNegocio;
    }

    public void setNomeDoNegocio(String nomeDoNegocio) {
        this.nomeDoNegocio = nomeDoNegocio;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(byte[] imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
