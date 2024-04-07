package projetopi.projetopi.dominio;

import projetopi.projetopi.dominio.*;

import java.time.LocalDateTime;

public interface iAgendavel {
    AgendaAux agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao);
}
