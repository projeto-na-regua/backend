package projetopi.projetopi.dto.response;

import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Cliente;

public class InfoUsuario {

    private String nome;

    private String email;

    private String celular;


    public InfoUsuario(Barbeiro barbeiro) {
        this.nome = barbeiro.getNome();
        this.email = barbeiro.getEmail();
        this.celular = barbeiro.getCelular();
    }

    public InfoUsuario(Cliente cliente) {
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.celular = cliente.getCelular();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }
}
