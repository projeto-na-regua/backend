package projetopi.projetopi.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Cliente extends Usuario implements iAgendavel {
    @OneToOne
    //name: nome da coluna 'fk', referencedColumnName: nome da coluna original da tabela referenciada:
    @JoinColumn(name = "barbearia_fk_endereco", referencedColumnName = "id_endereco")
    private Endereco endereco;

    public Cliente() {}

    public Cliente(Integer id, String nome, String email, String senha, String celular, byte[] imgPerfil,
                   boolean adm, Barbearia barbearia, Endereco endereco) {
        super(id, nome, email, senha, celular, imgPerfil, adm, barbearia);
        this.endereco = endereco;
    }

    @Override
    public AgendaAux agendar(Barbearia bb, Barbeiro b, Cliente c, Servico s, Especialidade e, Boolean concluido, Avaliacao avaliacao) {
        LocalDateTime dataHora = LocalDateTime.now();

        // Crie um novo objeto AgendaAux usando o construtor apropriado
        AgendaAux a = new AgendaAux(dataHora, s, b, c, bb, e, concluido, avaliacao);

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
