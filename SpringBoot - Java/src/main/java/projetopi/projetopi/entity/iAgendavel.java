package projetopi.projetopi.entity;

public interface iAgendavel {
    Agendamento agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao);
}
