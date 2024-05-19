package projetopi.projetopi.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import projetopi.projetopi.entity.Barbearia;

public class InfoBarbearia {


    private String nomeNegocio;

    @Email
    private String emailNegocio;

    @Size(max = 15)
    private String celularNegocio;

    @Size(max = 18)
    private String cnpj;

    @CPF
    private String cpf;

    private String descricao;

    public InfoBarbearia() {
    }

    public InfoBarbearia(Barbearia barbearia) {
        this.nomeNegocio = barbearia.getNomeNegocio();
        this.emailNegocio = barbearia.getEmailNegocio();
        this.celularNegocio = barbearia.getCelularNegocio();
        this.cnpj = barbearia.getCnpj();
        this.cpf = barbearia.getCpf();
        this.descricao = barbearia.getDescricao();
    }


    public Barbearia gerarBarbearia(){
      return new Barbearia(nomeNegocio, emailNegocio, celularNegocio, descricao);
    }

    public String getNomeNegocio() {
        return nomeNegocio;
    }

    public String getEmailNegocio() {
        return emailNegocio;
    }

    public String getCelularNegocio() {
        return celularNegocio;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDescricao() {
        return descricao;
    }
}
