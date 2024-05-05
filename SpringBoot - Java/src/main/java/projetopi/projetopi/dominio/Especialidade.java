package projetopi.projetopi.dominio;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Especialidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_especialidade")
    private Integer id;
    @Column(name="nome")
    private String nome;

    public Especialidade(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
