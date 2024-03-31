package projetopi.projetopi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AgendaAux {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeCliente;
    private String nomeBarbearia;
    private String enderecoBarbearia;
    private String numeroBarbearia;
    private String cep;
    private String cidade;
    private String nomeBarbeiro;
    private String nomeServico;
    private Integer tempoEstimado;
    private Double precoServico;
    private String horario;

    public AgendaAux(String nomeCliente, String nomeBarbearia,
                     String enderecoBarbearia, String numeroBarbearia, String cep,
                     String cidade, String nomeBarbeiro, String nomeServico,
                     Integer tempoEstimado, Double precoServico, String horario) {
        this.nomeCliente = nomeCliente;
        this.nomeBarbearia = nomeBarbearia;
        this.enderecoBarbearia = enderecoBarbearia;
        this.numeroBarbearia = numeroBarbearia;
        this.cep = cep;
        this.cidade = cidade;
        this.nomeBarbeiro = nomeBarbeiro;
        this.nomeServico = nomeServico;
        this.tempoEstimado = tempoEstimado;
        this.precoServico = precoServico;
        this.horario = horario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeBarbearia() {
        return nomeBarbearia;
    }

    public void setNomeBarbearia(String nomeBarbearia) {
        this.nomeBarbearia = nomeBarbearia;
    }

    public String getEnderecoBarbearia() {
        return enderecoBarbearia;
    }

    public void setEnderecoBarbearia(String enderecoBarbearia) {
        this.enderecoBarbearia = enderecoBarbearia;
    }

    public String getNumeroBarbearia() {
        return numeroBarbearia;
    }

    public void setNumeroBarbearia(String numeroBarbearia) {
        this.numeroBarbearia = numeroBarbearia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getNomeBarbeiro() {
        return nomeBarbeiro;
    }

    public void setNomeBarbeiro(String nomeBarbeiro) {
        this.nomeBarbeiro = nomeBarbeiro;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public Double getPrecoServico() {
        return precoServico;
    }

    public void setPrecoServico(Double precoServico) {
        this.precoServico = precoServico;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return """
                Caro %s, seu agendamento foi realizado!
                
                ----------------------
                
                Barbearia: %s
                Endereço: %s, %s - %s - %s
                Barbeiro: %s
                Serviço: %s
                Tempo estimado: %s minutos
                Preço: R$%.2f
                Horário: %s
                """.formatted(nomeCliente, nomeBarbearia, enderecoBarbearia, numeroBarbearia, cep, cidade, nomeBarbeiro, nomeServico, tempoEstimado, precoServico, horario);
    }
}
