package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.entity.Comentario;
import projetopi.projetopi.entity.Midia;
import projetopi.projetopi.entity.Postagem;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.repository.ComentarioRepository;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class ComentarioConsulta {

    private Integer id;
    private String nome;
    private String username;
    private String imgPerfil;
    private String conteudo;
    private String midia;
    private LocalDateTime dataCriacao;
    private Integer qtdCurtidas;

    public ComentarioConsulta(Comentario comentario) {
        this.id = comentario.getId();
        this.nome = comentario.getUsuario().getNome();
        this.username = comentario.getUsuario().getUsername();
        this.conteudo = comentario.getConteudo();
        this.dataCriacao = comentario.getDataCriacao();
    }

    public ComentarioConsulta(Comentario comentario, Integer qtdCurtidas) {
        this.id = comentario.getId();
        this.nome = comentario.getUsuario().getNome();
        this.username = comentario.getUsuario().getUsername();
        this.conteudo = comentario.getConteudo();
        this.dataCriacao = comentario.getDataCriacao();
        this.qtdCurtidas = qtdCurtidas;
    }

    public ComentarioConsulta(Comentario comentario, String imgPerfil) {
        this.id = comentario.getId();
        this.nome = comentario.getUsuario().getNome();
        this.username = comentario.getUsuario().getUsername();
        this.conteudo = comentario.getConteudo();
        this.dataCriacao = comentario.getDataCriacao();
        this.imgPerfil = imgPerfil;
    }

}
