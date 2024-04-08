package projetopi.projetopi.util;

import jakarta.validation.constraints.NotNull;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Endereco;
import projetopi.projetopi.repositorio.EnderecoRepository;

public class CadastroBarbearia {

    @NotNull
    private Barbearia barbearia;

    @NotNull
    private Barbeiro barbeiro;

    public Barbearia getBarbearia() {
        return barbearia;
    }

    public void setBarbearia(Barbearia barbearia) {
        this.barbearia = barbearia;
    }


    public Barbeiro getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Barbeiro barbeiro) {
        this.barbeiro = barbeiro;
    }
}
