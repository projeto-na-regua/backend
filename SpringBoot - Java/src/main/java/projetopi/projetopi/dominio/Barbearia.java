package projetopi.projetopi.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import projetopi.projetopi.util.Dia;

import java.time.LocalTime;
import java.util.Optional;

@Entity
public class Barbearia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_barbearia")
    private Integer id;

    @Column(name="nome_negocio")
    private String nomeDoNegocio;
    @JsonIgnore
    @Column(name="img_perfil")
    private byte[] imgPerfil;

    @Column(name = "email_negocio", nullable = true)
    @Email
    private String emailNegocio;

    @Size(max = 15)
    @Column(name = "celular_negocio", nullable = true)
    private String celularNegocio;

    @Column(name = "cnpj", nullable = true)
    @CNPJ
    private String cnpj;

    @Column(name = "cpf", nullable = true)
    @CPF
    private String cpf;

    @ManyToOne()
    @JoinColumn(name = "barbearia_fk_endereco", nullable = false)
    private Endereco endereco;

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
