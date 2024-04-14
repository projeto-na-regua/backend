package projetopi.projetopi.util;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import projetopi.projetopi.dominio.DiaSemana;

public class Perfil {

    @Column(name="nome_negocio")
    private String nomeDoNegocio;

    @Column(name = "email_negocio", nullable = true)
    @Email
    private String emailNegocio;

    @Size(max = 15)
    @Column(name = "celular_negocio", nullable = true)
    private String celularNegocio;

    @Size(max = 18)
    @Column(name = "cnpj", nullable = true)
    private String cnpj;

    @CPF
    private String cpf;

    private DiaSemana diaSemana;

    public String getNomeDoNegocio() {
        return nomeDoNegocio;
    }

    public void setNomeDoNegocio(String nomeDoNegocio) {
        this.nomeDoNegocio = nomeDoNegocio;
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

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }
}


