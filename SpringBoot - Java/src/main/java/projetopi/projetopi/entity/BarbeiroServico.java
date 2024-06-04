package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "usuario_servico")
@IdClass(BarbeiroServicoId.class)
@Getter
@Setter
public class BarbeiroServico {

    @Id
    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario")
    private Barbeiro barbeiro;

    @Id
    @ManyToOne
    @JoinColumn(name = "servico_id_servico")
    private Servico servico;

    @Id
    @ManyToOne
    @JoinColumn(name = "servico_fk_barbearia")
    private Barbearia barbearia;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarbeiroServico that = (BarbeiroServico) o;
        return Objects.equals(barbeiro, that.barbeiro) &&
                Objects.equals(servico, that.servico) &&
                Objects.equals(barbearia, that.barbearia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barbeiro, servico, barbearia);
    }
}
