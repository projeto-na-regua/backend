package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Getter
@Setter
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


}
