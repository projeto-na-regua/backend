package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;

    @Column(name="data_hora_fim_prevista")
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraFimPrevista;

    @Column(name="data_hora_concluido")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraConcluido;

    @Column(name="status")
    private String status;

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

    public Agendamento() {}

    public Agendamento(LocalDateTime dataHora, Servico servico, Barbeiro barbeiro,
                       Cliente cliente, Barbearia barbearia, String status, Avaliacao avaliacao) {
        this.dataHora = dataHora;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.barbearia = barbearia;
        this.status = status;
        this.avaliacao = avaliacao;
    }

    public Agendamento(LocalDateTime dataHora, Servico servico, Barbeiro barbeiro, Cliente cliente, Barbearia barbearia) {
        this.dataHora = dataHora;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.barbearia = barbearia;
    }

    public Agendamento(LocalDateTime dataHora, Servico servico, Barbeiro barbeiro, Barbearia barbearia) {
        this.dataHora = dataHora;
        this.servico = servico;
        this.barbeiro = barbeiro;
        this.barbearia = barbearia;
    }
}
