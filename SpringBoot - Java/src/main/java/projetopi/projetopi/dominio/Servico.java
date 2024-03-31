package projetopi.projetopi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeDoServico;
    private Double precoServico;
    private Integer tempoEstimado;

    public Servico() {
        // Construtor padrão necessário para o Hibernate
    }

    public Servico(String nomeDoServico, Double precoServico, Integer tempoEstimado) {
        this.nomeDoServico = nomeDoServico;
        this.precoServico = precoServico;
        this.tempoEstimado = tempoEstimado;
    }

    // Getters e Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeDoServico() {
        return nomeDoServico;
    }

    public void setNomeDoServico(String nomeDoServico) {
        this.nomeDoServico = nomeDoServico;
    }

    public Double getPrecoServico() {
        return precoServico;
    }

    public void setPrecoServico(Double precoServico) {
        this.precoServico = precoServico;
    }

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    @Override
    public String toString() {
        return "Servico{" +
                "id=" + id +
                ", nomeDoServico='" + nomeDoServico + '\'' +
                ", precoServico=" + precoServico +
                ", tempoEstimado=" + tempoEstimado +
                '}';
    }
}
