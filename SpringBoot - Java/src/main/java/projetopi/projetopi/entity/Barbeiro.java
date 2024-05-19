package projetopi.projetopi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Barbeiro extends Usuario implements iAgendavel {

    @Column(name="usuario_admin")
    private boolean adm;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="usuario_fk_barbearia")
    private Barbearia barbearia;


    @ManyToOne
    @JoinColumn(name="barbeiro_fk_especialidade", nullable = false)
    private Especialidade especialidade;

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

    @Override
    public Agendamento agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao) {
        LocalDateTime dataHora = LocalDateTime.now();

        // Crie um novo objeto AgendaAux usando o construtor apropriado
        Agendamento a = new Agendamento(dataHora, s, b, c, bb, e, concluido, avaliacao);

        System.out.println(a);

        return a;
    }

    public boolean isAdm() {
        return adm;
    }

    public void setAdm(boolean adm) {
        this.adm = adm;
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
}
