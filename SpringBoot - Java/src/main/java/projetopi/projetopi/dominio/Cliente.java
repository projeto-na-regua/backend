package projetopi.projetopi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Cliente extends Usuario implements iAgendavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Cliente(String nome, String telefone, String email) {
        super(nome, telefone, email);
    }

    @Override
    public AgendaAux agendar(Barbearia b, Barbeiro bb, Cliente c, Servico s) {

        LocalDateTime horario = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'h'mm");

        AgendaAux a = new AgendaAux(c.getNome(), b.getNomeDoNegocio(), b.getLogradouro(), b.getNumero(), b.getCep(), b.getCidade(), bb.getNome(), s.getNomeDoServico(),s.getTempoEstimado(), s.getPrecoServico(), horario.format(formatter));

        System.out.println(a);

        return a;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
