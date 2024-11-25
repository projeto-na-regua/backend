package projetopi.projetopi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_notificacao")
@Getter
@Setter
@NoArgsConstructor
public class TipoNotificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_notificacao")
    private Integer id;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public TipoNotificacao(String nome) {
        this.nome = nome;
        this.dataCriacao = LocalDateTime.now();
    }
}