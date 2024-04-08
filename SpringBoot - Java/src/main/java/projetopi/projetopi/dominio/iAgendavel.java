package projetopi.projetopi.dominio;

public interface iAgendavel {
    Agendamento agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao);
}
