package projetopi.projetopi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.DiaSemana;
import projetopi.projetopi.dominio.Endereco;

import java.time.LocalTime;

public class PerfilBarbearia {


    private String nomeDoNegocio;
    @Email
    private String emailNegocio;

    @Size(max = 15)
    private String celularNegocio;

    private String descricao;

    @Size(max = 18)
    private String cnpj;

    @CPF
    private String cpf;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
}
