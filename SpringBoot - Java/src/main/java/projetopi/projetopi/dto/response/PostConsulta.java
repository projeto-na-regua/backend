package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projetopi.projetopi.entity.Postagem;
import projetopi.projetopi.entity.Usuario;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostConsulta {
    private Integer id;
    private String nome;
    private String username;
    private String imgPerfil;
    private String conteudo;
    private String midia;
    private LocalDateTime dataCriacao;
    private Integer qtdCurtidas;
    private Integer qtdComentarios;

    public PostConsulta(Postagem post) {
        this.id = post.getId();
        this.nome = post.getUsuario().getNome();
        this.username = post.getUsuario().getUsername();
        this.conteudo = post.getConteudo();
        this.dataCriacao = post.getDataCriacao();
    }

    public PostConsulta(Postagem post, Integer qtdComentarios, Integer qtdCurtidas) {
        this.id = post.getId();
        this.nome = post.getUsuario().getNome();
        this.username = post.getUsuario().getUsername();
        this.conteudo = post.getConteudo();
        this.dataCriacao = post.getDataCriacao();
        this.qtdCurtidas = qtdCurtidas;
        this.qtdComentarios = qtdComentarios;
    }

    public PostConsulta(Postagem post, String imgPerfil) {
        this.id = post.getId();
        this.nome = post.getUsuario().getNome();
        this.username = post.getUsuario().getUsername();
        this.conteudo = post.getConteudo();
        this.dataCriacao = post.getDataCriacao();
        this.imgPerfil = imgPerfil;
    }
}
