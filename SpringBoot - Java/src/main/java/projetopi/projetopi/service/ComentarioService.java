package projetopi.projetopi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.ComentarioConsulta;
import projetopi.projetopi.entity.Comentario;
import projetopi.projetopi.entity.Midia;
import projetopi.projetopi.entity.Postagem;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.AcessoNegadoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.ComentarioRepository;
import projetopi.projetopi.repository.CurtidaRepository;
import projetopi.projetopi.repository.MidiaRepository;
import projetopi.projetopi.repository.PostagemRepository;
import projetopi.projetopi.util.Global;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private Global global;

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private MidiaRepository midiaRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private NotificacoesService notificacoesService;

    public ComentarioConsulta comentar(String token, String texto, MultipartFile imagem, Integer idComentario) {

        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");

        Usuario usuario = global.getUsuarioByToken(token);
        Postagem post = postagemRepository.findById(idComentario).orElseThrow(
                () -> new RecursoNaoEncontradoException("Postagem não encontrada para o id: ", idComentario));
        Comentario comentario = comentarioRepository.save(new Comentario(texto, post, usuario));

        boolean existeMidia = imagem != null && !imagem.isEmpty();
        boolean existeImagemPerfil = comentario.getUsuario().getImgPerfil() != null && !comentario.getUsuario().getImgPerfil().isEmpty();
        Midia midiaSalvo = new Midia();

        if (imagem != null && !imagem.isEmpty()) {
            String fileName = imageService.upload(imagem, "comunidade");
            Midia midia = new Midia(fileName, comentarioRepository.findById(comentario.getId()).get());
            midiaSalvo = midiaRepository.save(midia);
        }

        ComentarioConsulta dto = new ComentarioConsulta(comentario, curtidaRepository.countByComentario_IdAndIsActiveTrue(comentario.getId()));
        dto.setMidia(existeMidia ? imageService.getImgURL(midiaSalvo.getArquivo(), "comunidade") : null);
        dto.setImgPerfil(existeImagemPerfil ? imageService.getImgURL(dto.getImgPerfil(), "usuario") : null);
        notificacoesService.notificarComentario(token, comentario);
        return dto;
    }

    public ComentarioConsulta getComentario(String token, Integer id) {
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");

        Comentario comentario = comentarioRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Postagem não encontrada para o id: ", id));

        ComentarioConsulta dto = new ComentarioConsulta(comentario);
        List<Midia> midias = midiaRepository.findByComentario(comentario);

        dto.setMidia(midias.isEmpty() ?  null : imageService.getImgURL(midias.get(0).getArquivo(), "comunidade"));
        dto.setImgPerfil(comentario.getUsuario().getImgPerfil() == null? null : imageService.getImgURL(comentario.getUsuario().getImgPerfil(), "usuario") );

        return new ComentarioConsulta(comentario,  curtidaRepository.countByComentario_IdAndIsActiveTrue(comentario.getId()));
    }

    public List<ComentarioConsulta> getComentariosByPost(String token, Integer idPostagem) {
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");
        List<Comentario> comentarios = comentarioRepository.findAllActiveByPostagemId(idPostagem);

        if (comentarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return this.toDto(comentarios);
    }

    public void delete(String token, Integer id){
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");

        Usuario usuario = global.getUsuarioByToken(token);

        Comentario comentario = comentarioRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Postagem não encontrada para o id: ", id));

        if(!comentario.getUsuario().getId().equals(usuario.getId())){
            throw new AcessoNegadoException("Postagem");
        }

        comentario.setIsActive(false);
        comentario.setId(comentario.getId());
        comentarioRepository.save(comentario);
    }


    public List<ComentarioConsulta> toDto(List<Comentario> comentarios){

        List<ComentarioConsulta> dtos = new ArrayList<>();

        for (Comentario comentario : comentarios){
            ComentarioConsulta comentarioConsulta = new ComentarioConsulta(comentario,  curtidaRepository.countByComentario_IdAndIsActiveTrue(comentario.getId()));
            List<Midia> midias = midiaRepository.findByComentario(comentario);
            comentarioConsulta.setMidia(midias.isEmpty() ?  null : imageService.getImgURL(midias.get(0).getArquivo(), "comunidade"));
            comentarioConsulta.setImgPerfil(comentario.getUsuario().getImgPerfil() == null? null : imageService.getImgURL(comentario.getUsuario().getImgPerfil(), "usuario") );
            dtos.add(comentarioConsulta);
        }

        return dtos;
    }


}
