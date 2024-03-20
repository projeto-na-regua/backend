package projetopi.projetopi;

public class Servico {
    private String nomeDoServico;
    private Double precoServico;
    private Integer tempoEstimado;


    public Servico(String nomeDoServico, Double precoServico, Integer tempoEstimado) {
        this.nomeDoServico = nomeDoServico;
        this.precoServico = precoServico;
        this.tempoEstimado = tempoEstimado;
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
                "nomeDoServico='" + nomeDoServico + '\'' +
                ", precoServico=" + precoServico +
                ", tempoEstimado=" + tempoEstimado +
                '}';
    }
}
