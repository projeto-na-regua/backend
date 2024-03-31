package projetopi.projetopi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import projetopi.projetopi.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Barbeiro extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean administrador;

    private List<Servico> servicos;

    public Barbeiro(String nome, String telefone, String email, Boolean administrador) {
        super(nome, telefone, email);
        this.administrador = administrador;
        List<Servico> servicos = new ArrayList<>();
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
}
