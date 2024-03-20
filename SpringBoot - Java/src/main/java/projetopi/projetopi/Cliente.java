package projetopi.projetopi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente extends Usuario implements iAgendavel{


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


}
