package projetopi.projetopi.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="imgs_galeria")
@Getter
@Setter
public class ImgsGaleria {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imgs_galeria")
    private Integer id;

    @Column(name="imagem")
    private String imagem;

    @Column(name="descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name="usuario_id_usuario", nullable = false)
    private Cliente cliente;

    public ImgsGaleria(String imagem, String descricao, Cliente cliente) {
        this.imagem = imagem;
        this.descricao = descricao;
        this.cliente = cliente;
    }

    public ImgsGaleria() {
    }
}
