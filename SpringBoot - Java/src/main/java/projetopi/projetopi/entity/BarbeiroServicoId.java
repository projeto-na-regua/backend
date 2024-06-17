package projetopi.projetopi.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
public class BarbeiroServicoId implements Serializable {

    private Integer barbeiro;
    private Integer servico;
    private Integer barbearia;

    public BarbeiroServicoId() {}

    public BarbeiroServicoId(Integer barbeiro, Integer servico, Integer barbearia) {
        this.barbeiro = barbeiro;
        this.servico = servico;
        this.barbearia = barbearia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarbeiroServicoId that = (BarbeiroServicoId) o;
        return Objects.equals(barbeiro, that.barbeiro) && Objects.equals(servico, that.servico) && Objects.equals(barbearia, that.barbearia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barbeiro, servico, barbearia);
    }
}