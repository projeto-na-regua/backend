package projetopi.projetopi.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.DiaSemana;
import projetopi.projetopi.dominio.Endereco;
import projetopi.projetopi.util.Dia;

import java.security.cert.TrustAnchor;

public class CadastroBarbearia {


    private String nomeDoNegocio;

    @CNPJ
    private String cnpj;

    @CPF
    private String cpf;

    @Size(max = 9)
    private String cep;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String cidade;

    private String nomeUsuario;

    @Email
    private String email;

    @Size(min = 8, max = 12)
    @NotBlank
    private String senha;

   private String estado;

   @Size(max = 15)
   private String celularUsuario;


   public DiaSemana[] gerarSemena(){
        DiaSemana[] semana = new DiaSemana[7];

        semana[0] = new DiaSemana(Dia.SEG);
        semana[1] = new DiaSemana(Dia.TER);
        semana[2] = new DiaSemana(Dia.QUA);
        semana[3] = new DiaSemana(Dia.QUI);
        semana[4] = new DiaSemana(Dia.SEX);
        semana[5] = new DiaSemana(Dia.SAB);
        semana[6] = new DiaSemana(Dia.DOM);

        return semana;

    }

   public Barbearia gerarBarbearia(){

       Barbearia barbearia = new Barbearia();

       barbearia.setNomeDoNegocio(nomeDoNegocio);

       if (cnpj.isEmpty()){
           barbearia.setCpf(cpf);
       }else {
           barbearia.setCnpj(cnpj);
       }


       return barbearia;
   }

   public Endereco gerarEndereco(){

       Endereco endereco = new Endereco();

       endereco.setCep(cep);
       endereco.setLogradouro(logradouro);
       endereco.setNumero(numero);
       endereco.setCidade(cidade);
       endereco.setEstado(estado);

       if (!complemento.isEmpty()){
           endereco.setComplemento(complemento);
       }

       return endereco;
   }

   public Barbeiro gerarBarbeiro(){

       Barbeiro barbeiro = new Barbeiro();

       barbeiro.setNome(nomeUsuario);
       barbeiro.setEmail(email);
       barbeiro.setSenha(senha);
       barbeiro.setAdm(true);
       barbeiro.setCelular(celularUsuario);

       return barbeiro;

   }


    public String getNomeDoNegocio() {
        return nomeDoNegocio;
    }

    public void setNomeDoNegocio(String nomeDoNegocio) {
        this.nomeDoNegocio = nomeDoNegocio;
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCelularUsuario() {
        return celularUsuario;
    }
}
