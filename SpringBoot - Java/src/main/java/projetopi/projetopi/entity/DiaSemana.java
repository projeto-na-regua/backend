package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import projetopi.projetopi.util.Dia;

import java.time.LocalTime;

@Entity
public class DiaSemana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_dia_semana", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Dia nome;

    private LocalTime horaAbertura;
    private LocalTime horaFechamento;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ds_id_barbearia")
    private Barbearia barbearia;

    public DiaSemana() {
    }

    public DiaSemana(Dia nome) {
        this.nome = nome;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dia getNome() {
        return nome;
    }

    public void setNome(Dia nome) {
        this.nome = nome;
    }

    public LocalTime getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(LocalTime horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public LocalTime getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(LocalTime horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public Barbearia getBarbearia() {
        return barbearia;
    }

    public void setBarbearia(Barbearia barbearia) {
        this.barbearia = barbearia;
    }
}
