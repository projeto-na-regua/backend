package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.util.Dia;

import java.time.LocalTime;

@Entity
@Getter
@Setter
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
}
