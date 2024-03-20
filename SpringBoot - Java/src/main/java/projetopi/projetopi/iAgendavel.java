package projetopi.projetopi;

import java.time.LocalDateTime;

public interface iAgendavel {

    AgendaAux agendar(Barbearia b, Barbeiro bb, Cliente c, Servico s);
}
