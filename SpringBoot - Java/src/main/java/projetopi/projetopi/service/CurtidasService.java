package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetopi.projetopi.dto.response.CurtidaResponse;
import projetopi.projetopi.entity.Comentario;
import projetopi.projetopi.entity.Curtida;
import projetopi.projetopi.entity.Postagem;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.ComentarioRepository;
import projetopi.projetopi.repository.CurtidaRepository;
import projetopi.projetopi.repository.PostagemRepository;
import projetopi.projetopi.util.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurtidasService {

    @Autowired
    private CurtidaRepository curtidaRepository;
    @Autowired
    private  PostagemRepository postagemRepository;
    @Autowired
    private  ComentarioRepository comentarioRepository;
    @Autowired
    private  Global global;
    @Autowired
    private NotificacoesService notificacoesService;


    public CurtidaResponse curtir(String token, Integer id, String tipo) {
        global.validaBarbeiro(token, "barbeiro");
        Usuario usuario = global.getUsuarioByToken(token);

        CurtidaResponse dto = new CurtidaResponse();

        if (validarSeEPostagem(tipo)) {
            Optional<Curtida> curtida = curtidaRepository.findByUsuario_IdAndPostagem_Id(usuario.getId(), id);
            if (curtida.isPresent()) {
                Curtida existingCurtida = curtida.get();
                if (!existingCurtida.getIsActive()) {
                    existingCurtida.setId(existingCurtida.getId());
                    existingCurtida.setIsActive(true);
                    Curtida nvCurtida = curtidaRepository.save(existingCurtida);
                    notificacoesService.notificarCurtida(usuario,nvCurtida, "postagem");
                } else {
                    dto = descurtirPost(usuario, id);
                }
            } else {
                dto = curtirPost(usuario, id);
            }
        } else {
            Optional<Curtida> curtida = curtidaRepository.findByUsuario_IdAndComentario_Id(usuario.getId(), id);
            if (curtida.isPresent()) {
                Curtida existingCurtida = curtida.get();
                if (!existingCurtida.getIsActive()) {
                    existingCurtida.setId(existingCurtida.getId());
                    existingCurtida.setIsActive(true);
                    Curtida nvCurtida = curtidaRepository.save(existingCurtida);
                    notificacoesService.notificarCurtida(usuario,nvCurtida, "comentario");

                } else {
                    dto = descurtirComentario(usuario, id);
                }
            } else {
                dto = curtirComentario(usuario, id);
            }
        }

        return dto;
    }

    public CurtidaResponse curtirPost(Usuario usuario, Integer idPost) {
        Postagem post = postagemRepository.findById(idPost).orElseThrow(
                () -> new RecursoNaoEncontradoException("Postagem não encontrada para o id: ", idPost));

        Curtida curtida = curtidaRepository.save(new Curtida(usuario, post));
        notificacoesService.notificarCurtida(usuario,curtida, "postagem");
        return new CurtidaResponse(curtida);
    }

    public CurtidaResponse curtirComentario(Usuario usuario, Integer idComentario) {
        Comentario comentario = comentarioRepository.findById(idComentario).orElseThrow(
                () -> new RecursoNaoEncontradoException("Comentário não encontrado para o id: ", idComentario));
        Curtida curtida = curtidaRepository.save(new Curtida(usuario, comentario));
        notificacoesService.notificarCurtida(usuario,curtida, "comentario");
        return new CurtidaResponse(curtida);

    }

    public CurtidaResponse descurtirPost(Usuario usuario, Integer idPost) {
        Curtida curtida = curtidaRepository.findByUsuario_IdAndPostagem_IdAndIsActiveTrue(usuario.getId(), idPost)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curtida não encontrada para o id: ", idPost));
        curtida.setIsActive(false);
        curtida.setId(curtida.getId());
        return new CurtidaResponse(curtidaRepository.save(curtida));
    }

    public CurtidaResponse descurtirComentario(Usuario usuario, Integer idComentario) {
        Curtida curtida = curtidaRepository.findByUsuario_IdAndComentario_IdAndIsActiveTrue(usuario.getId(), idComentario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curtida não encontrada para o id: ", idComentario));
        curtida.setIsActive(false);
        curtida.setId(curtida.getId());
        return new CurtidaResponse(curtidaRepository.save(curtida));
    }

    public CurtidaResponse getCurtida(String token, Integer id, String tipo) {
        global.validaBarbeiro(token, "barbeiro");
        Usuario usuario = global.getUsuarioByToken(token);

        Optional<Curtida> curtida;
        if (validarSeEPostagem(tipo)) {
            curtida = curtidaRepository.findByUsuario_IdAndPostagem_IdAndIsActiveTrue(usuario.getId(), id);
        } else {
            curtida = curtidaRepository.findByUsuario_IdAndComentario_IdAndIsActiveTrue(usuario.getId(), id);
        }
        return curtida.map(CurtidaResponse::new).orElse(null);
    }

    public List<CurtidaResponse> listCurtida(String token, Integer id, String tipo) {
        global.validaBarbeiro(token, "barbeiro");
        Usuario usuario = global.getUsuarioByToken(token);

        if (validarSeEPostagem(tipo)) {
            List<Curtida> curtidas = curtidaRepository.findByPostagem_IdAndIsActiveTrue(id);
            return toDto(curtidas);
        } else {
            List<Curtida> curtidas = curtidaRepository.findByComentario_IdAndIsActiveTrue(id);
            return toDto(curtidas);
        }
    }

    public Integer getQtdCurtidas(String token, Integer id, String tipo) {
        global.validaBarbeiro(token, "barbeiro");
        Usuario usuario = global.getUsuarioByToken(token);

        if (validarSeEPostagem(tipo)) {
            return curtidaRepository.countByPostagem_IdAndIsActiveTrue(id);
        } else {
            return curtidaRepository.countByComentario_IdAndIsActiveTrue(id);
        }
    }

    public boolean validarSeEPostagem(String tipo) {
        return tipo.equalsIgnoreCase("postagem");
    }

    public List<CurtidaResponse> toDto(List<Curtida> curtidas) {
        List<CurtidaResponse> dtos = new ArrayList<>();
        for (Curtida c : curtidas) {
            dtos.add(new CurtidaResponse(c));
        }
        return dtos;
    }
}
