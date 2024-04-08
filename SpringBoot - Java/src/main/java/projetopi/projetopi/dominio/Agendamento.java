package projetopi.projetopi.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_agendamento", nullable = false)
    private Integer id;
    @Column(name="data_hora")
    private LocalDateTime dataHora;
    @Column(name="concluido")
    private Boolean concluido;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="ag_fk_servico")
    private Servico servico;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="ag_fk_barbeiro")
    private Barbeiro barbeiro;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="ag_fk_cliente")
    private Cliente cliente;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="ag_fk_barbearia")
    private Barbearia barbearia;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="ag_fk_especialidade")
    private Especialidade especialidade;

    @OneToOne
    @PrimaryKeyJoinColumn(name="ag_fk_avaliacao")
    private Avaliacao avaliacao;

    public Agendamento(){}

    public Agendamento(LocalDateTime dataHora, Servico servico, Barbeiro barbeiro,
                       Cliente cliente, Barbearia barbearia, Especialidade especialidade, Boolean concluido, Avaliacao avaliacao) {
        this.dataHora = dataHora;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.barbearia = barbearia;
        this.especialidade = especialidade;
        this.concluido = concluido;
        this.avaliacao = avaliacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Barbeiro getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Barbeiro barbeiro) {
        this.barbeiro = barbeiro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Barbearia getBarbearia() {
        return barbearia;
    }

    public void setBarbearia(Barbearia barbearia) {
        this.barbearia = barbearia;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }
}
