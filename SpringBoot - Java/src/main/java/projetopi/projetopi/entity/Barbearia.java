package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import projetopi.projetopi.dto.response.BarbeariaConsulta;

import java.util.List;

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

    @Column(name="img_perfil")
    private String imgPerfil;

    @Column(name="img_banner")
    private String imgBanner;

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

    @OneToMany(mappedBy = "barbearia")
    private List<Servico> servicos;

    public Barbearia(String nomeNegocio) {
        this.nomeNegocio = nomeNegocio;
    }

    public Barbearia() {}

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

    public Barbearia(BarbeariaConsulta barbeariaConsulta) {
        this.nomeNegocio = barbeariaConsulta.getNomeNegocio();
        this.emailNegocio = barbeariaConsulta.getEmailNegocio();
        this.celularNegocio = barbeariaConsulta.getCelularNegocio();
        this.descricao = barbeariaConsulta.getDescricao();
    }



    public Barbearia(String nomeNegocio, String emailNegocio, String celularNegocio, String descricao) {
        this.nomeNegocio = nomeNegocio;
        this.emailNegocio = emailNegocio;
        this.celularNegocio = celularNegocio;
        this.descricao = descricao;
    }


}
