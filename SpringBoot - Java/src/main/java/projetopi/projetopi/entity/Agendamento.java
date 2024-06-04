package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
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
    @PrimaryKeyJoinColumn(name="servico_id_servico")
    private Servico servico;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="barbeiro_id_usuario")
    private Barbeiro barbeiro;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="cliente_id_usuario")
    private Cliente cliente;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "barbearia_id_barbearia")
    private Barbearia barbearia;

    @OneToOne
    @JoinColumn(name="ag_fk_avaliacao", nullable = true)
    private Avaliacao avaliacao;

    public Agendamento(){}

    public Agendamento(LocalDateTime dataHora, Servico servico, Barbeiro barbeiro,
                       Cliente cliente, Barbearia barbearia, Boolean concluido, Avaliacao avaliacao) {
        this.dataHora = dataHora;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.barbearia = barbearia;
        this.concluido = concluido;
        this.avaliacao = avaliacao;
    }

    public Agendamento(LocalDateTime dataHora, Servico servico, Barbeiro barbeiro, Cliente cliente, Barbearia barbearia) {
        this.dataHora = dataHora;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.barbearia = barbearia;
    }
}
