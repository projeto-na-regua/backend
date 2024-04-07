package projetopi.projetopi.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Barbeiro extends Usuario implements iAgendavel {
    public Barbeiro(){}

    public Barbeiro(Integer id, String nome, String email, String senha, String celular, byte[] imgPerfil,
                    boolean adm, Barbearia barbearia) {
        super(id, nome, email, senha, celular, imgPerfil, adm, barbearia);
    }

    @Override
    public AgendaAux agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao) {
        LocalDateTime dataHora = LocalDateTime.now();

        // Crie um novo objeto AgendaAux usando o construtor apropriado
        AgendaAux a = new AgendaAux(dataHora, s, b, c, bb, e, concluido, avaliacao);

        System.out.println(a);

        return a;
    }
}
