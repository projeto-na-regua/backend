package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Barbeiro extends Usuario implements iAgendavel {

    @Column(name="usuario_admin")
    private boolean adm;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="usuario_fk_barbearia")
    private Barbearia barbearia;

    @ManyToOne
    @JoinColumn(name="barbeiro_fk_especialidade", nullable = true)
    private Especialidade especialidade;

    @OneToMany(mappedBy = "barbeiro")
    private Set<BarbeiroServico> usuarioServicos;

    public Barbeiro(String nome, String email, String celular) {
        super(nome, email, celular);
    }

    public Barbeiro() {
    }

    public Barbeiro(String nome) {
        super(nome);
    }

    public Barbeiro(String nome, String email, String celular, boolean adm, Barbearia barbearia) {
        super(nome, email, celular);
        this.adm = adm;
        this.barbearia = barbearia;
    }

    public Barbeiro(Integer id) {
        super(id);
    }

    public Barbeiro(Barbeiro barbeiro) {}

    @Override
    public Agendamento agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao) {
        LocalDateTime dataHora = LocalDateTime.now();

        // Crie um novo objeto AgendaAux usando o construtor apropriado
        Agendamento a = new Agendamento();

        System.out.println(a);

        return a;
    }



}
