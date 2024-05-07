package projetopi.projetopi.dto.response;

import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Cliente;
import projetopi.projetopi.dominio.Usuario;

public class TokenConsulta {

    private String token;

    private String tipo;

    private boolean adm;

    private Integer idBarbearia;

    public TokenConsulta(Cliente c, String token ) {
        this.token = token;
        this.tipo = c.getDtype();
    }

    public TokenConsulta(Barbeiro barbeiro, String token ) {
        this.token = token;
        this.tipo = barbeiro.getDtype();
        this.adm = barbeiro.isAdm();
        this.idBarbearia = barbeiro.getBarbearia().getId();
    }

    public String getToken() {
        return token;
    }

    public boolean isAdm() {
        return adm;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getIdBarbearia() {
        return idBarbearia;
    }
}
