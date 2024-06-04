package projetopi.projetopi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Cliente")
public class Cliente extends Usuario implements iAgendavel {

    public Cliente(String nome, String email, String celular) {
        super(nome, email, celular);
    }

    public Cliente() {
    }

    public Cliente(String nome, String email, String senha, String celular, Endereco endereco) {
        super(nome, email, senha, celular, endereco);
    }

    @Override
    public Agendamento agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao) {
        LocalDateTime dataHora = LocalDateTime.now();

        // Crie um novo objeto AgendaAux usando o construtor apropriado
        Agendamento a = new Agendamento();

        System.out.println(a);

        return a;
    }

}
