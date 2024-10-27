package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.PostConsulta;
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
public class PostagemService {


    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private MidiaRepository midiaRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private Global global;

    @Autowired
    private NotificacoesService notificacoesService;

    public PostConsulta postar(String token, String texto, MultipartFile imagem) {

        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");

        Usuario usuario = global.getUsuarioByToken(token);

        Postagem nvPost = new Postagem(texto, usuario);

        Postagem postSalvo = postagemRepository.save(nvPost);

        boolean existeMidia = imagem != null && !imagem.isEmpty();
        boolean existeImagemPerfil = postSalvo.getUsuario().getImgPerfil() != null && !postSalvo.getUsuario().getImgPerfil().isEmpty();
        Midia midiaSalvo = new Midia();

        if (imagem != null && !imagem.isEmpty()) {
            String fileName = imageService.upload(imagem, "comunidade");
            Midia midia = new Midia(fileName, postSalvo);
            midiaSalvo = midiaRepository.save(midia);
        }

        PostConsulta dto = new PostConsulta(postSalvo, comentarioRepository.countActiveByPostagem_Id(postSalvo.getId()), curtidaRepository.countByPostagem_IdAndIsActiveTrue(postSalvo.getId()));
        dto.setMidia(existeMidia ? imageService.getImgURL(midiaSalvo.getArquivo(), "comunidade") : null);
        dto.setImgPerfil(existeImagemPerfil ? imageService.getImgURL(dto.getImgPerfil(), "usuario") : null);

        return dto;
    }

    public PostConsulta getPost(String token, Integer id) {
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");

        Postagem post = postagemRepository.findById(id).orElseThrow(
                    () -> new RecursoNaoEncontradoException("Postagem não encontrada para o id: ", id));

        PostConsulta dto = new PostConsulta(post);
        List<Midia> midias = midiaRepository.findByPostagem(post);

        dto.setMidia(midias.isEmpty() ?  null : imageService.getImgURL(midias.get(0).getArquivo(), "comunidade"));
        dto.setImgPerfil(post.getUsuario().getImgPerfil() == null? null : imageService.getImgURL(dto.getImgPerfil(), "usuario") );

        return new PostConsulta(post, comentarioRepository.countActiveByPostagem_Id(post.getId()), curtidaRepository.countByPostagem_IdAndIsActiveTrue(post.getId()));
    }

    public List<PostConsulta> getUltimosPosts(String token) {
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");
        List<Postagem> posts = postagemRepository.findTop10ByOrderByDataCriacaoDesc();

        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }
        return this.toDto(posts);
    }

    public List<PostConsulta> getByUsuario(String token, Integer idUsuario) {
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");
        List<Postagem> posts = postagemRepository.findAllByUsuario_IdAndIsActiveTrue(idUsuario);
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }
        return this.toDto(posts);
    }

    public List<PostConsulta> getMyPosts(String token) {
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");
        Usuario usuario = global.getUsuarioByToken(token);
        List<Postagem> posts = postagemRepository.findAllByUsuario_IdAndIsActiveTrue(usuario.getId());
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }
        return this.toDto(posts);
    }

    public void deletePost(String token, Integer id){
        global.validarUsuarioExiste(token);
        global.validaBarbeiro(token, "Barbeiro");
        Usuario usuario = global.getUsuarioByToken(token);

        Postagem post = postagemRepository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Postagem não encontrada para o id: ", id));

        if(!post.getUsuario().getId().equals(usuario.getId())){
            throw new AcessoNegadoException("Postagem");
        }

        post.setIsActive(false);
        post.setId(post.getId());
        postagemRepository.save(post);
    }


    public List<PostConsulta> toDto(List<Postagem> posts){

        List<PostConsulta> dtos = new ArrayList<>();

        for (Postagem post : posts){
            PostConsulta postConsulta = new PostConsulta(post, comentarioRepository.countActiveByPostagem_Id(post.getId()),
                    curtidaRepository.countByPostagem_IdAndIsActiveTrue(post.getId()));
            List<Midia> midias = midiaRepository.findByPostagem(post);
            postConsulta.setMidia(midias.isEmpty() ?  null : imageService.getImgURL(midias.get(0).getArquivo(), "comunidade"));
            postConsulta.setImgPerfil(post.getUsuario().getImgPerfil() == null? null : imageService.getImgURL(postConsulta.getImgPerfil(), "usuario") );
            dtos.add(postConsulta);
        }

        return dtos;
    }


}
