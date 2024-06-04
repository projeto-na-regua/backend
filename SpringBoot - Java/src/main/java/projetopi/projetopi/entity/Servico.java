package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_servico")
    private Integer id;
    @Column(name="preco")
    private Double preco;
    @Column(name="descricao")
    private String descricao;
    @Column(name="tipo_servico")
    private String tipoServico;
    @Column(name="tempo_estimado", nullable = false)
    private Integer tempoEstimado;
    @ManyToOne
    @JoinColumn(name="servico_fk_barbearia")
    private Barbearia barbearia;
    @OneToMany(mappedBy = "servico")
    private Set<BarbeiroServico> barbeiroServicos = new HashSet<>();


    public Servico(Integer id, Double preco, String descricao, String tipoServico, Integer tempoEstimado, Barbearia barbearia) {
        this.id = id;
        this.preco = preco;
        this.descricao = descricao;
        this.tipoServico = tipoServico;
        this.tempoEstimado = tempoEstimado;
        this.barbearia = barbearia;
    }

    public Servico(Integer id,Double preco, String descricao, String tipoServico, Integer tempoEstimado, String nomebarbeiro) {
        this.id = id;
        this.preco = preco;
        this.descricao = descricao;
        this.tipoServico = tipoServico;
        this.tempoEstimado = tempoEstimado;
    }

    public Servico() {}

    public Set<Barbeiro> getBarbeiros() {
        if (barbeiroServicos == null) {
            return new HashSet<>();
        }
        return barbeiroServicos.stream()
                .map(BarbeiroServico::getBarbeiro)
                .collect(Collectors.toSet());
    }
}
