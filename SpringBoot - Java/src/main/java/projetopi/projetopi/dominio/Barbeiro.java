package projetopi.projetopi.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Barbeiro extends Usuario implements iAgendavel {

    @Column(name="usuario_admin")
    private boolean adm;

    @ManyToOne
    @JoinColumn(name="usuario_fk_barbearia", nullable = true)
    private Barbearia barbearia;


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
}
