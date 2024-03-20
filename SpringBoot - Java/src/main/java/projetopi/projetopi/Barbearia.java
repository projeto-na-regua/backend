package projetopi.projetopi;

import java.time.LocalDateTime;

public class Barbearia implements iAgendavel{

    private String nomeDoNegocio;
    private String celular;
    private String email;
    private String cep;
    private String logradouro;
    private String numero;
    private String cidade;

    public Barbearia(String nomeDoNegocio, String celular, String email, String cep, String logradouro, String numero, String cidade) {
        this.nomeDoNegocio = nomeDoNegocio;
        this.celular = celular;
        this.email = email;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "Barbearia{" +
                "nomeDoNegocio='" + nomeDoNegocio + '\'' +
                ", celular='" + celular + '\'' +
                ", email='" + email + '\'' +
                ", cep='" + cep + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", numero='" + numero + '\'' +
                ", cidade='" + cidade + '\'' +
                '}';
    }

    @Override
        public AgendaAux agendar(Barbearia b, Barbeiro bb, Cliente c, Servico s) {

        return null;

            }
}
