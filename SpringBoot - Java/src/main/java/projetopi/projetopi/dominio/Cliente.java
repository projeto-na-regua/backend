package projetopi.projetopi.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Cliente extends Usuario implements iAgendavel {
    @OneToOne
    @JoinColumn(name = "usuario_fk_endereco", nullable = true)
    private Endereco endereco;


    public Cliente(String nome, String email, String celular) {
        super(nome, email, celular);
    }

    public Cliente() {
    }

    @Override
    public Agendamento agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao) {
        LocalDateTime dataHora = LocalDateTime.now();

        // Crie um novo objeto AgendaAux usando o construtor apropriado
        Agendamento a = new Agendamento(dataHora, s, b, c, bb, e, concluido, avaliacao);

        System.out.println(a);

        return a;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
