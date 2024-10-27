package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.CountTipoNotificacoesConsultas;
import projetopi.projetopi.dto.response.NotificacaoConsulta;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NotificacoesService {


    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private TipoNotificacaoRepository tipoNotificacaoRepository;

    @Autowired
    private Global global;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;


    public NotificacaoConsulta notificarMensagem(String token, Chat chat){

        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        Usuario usuarioNotificado = global.getUsuarioByToken(token);

        TipoNotificacao tipoNotificacao = getTipoNotificacao("mensagem");
        String texto;

        boolean clientSender = true;

        if (usuario.getDtype().equalsIgnoreCase("Barbeiro")) {
            Barbeiro barbeiro = barbeiroRepository.findById(usuario.getId()).get();
            if(chat.getBarbearia().getId().equals(barbeiro.getBarbearia().getId())){
                clientSender = false;
            }
        }

        if (clientSender){
            texto = "A barbearia %s te enviou mensagem".formatted(chat.getBarbearia().getNomeNegocio());
            usuarioNotificado = usuarioRepository.findAllByBarbeariaId(chat.getBarbearia().getId()).get(0);
        }else{
            texto = "O usuario %s te enviou uma mendagem".formatted(chat.getUsuario().getNome());
            usuarioNotificado = chat.getUsuario();
        }

        Notificacao novaNotificacao = notificacaoRepository.save(new Notificacao(usuario, tipoNotificacao, chat.getId(), texto, usuarioNotificado));

        return new NotificacaoConsulta(novaNotificacao, "Você tem uma nova mensagem", chat.getId());
    }



    public NotificacaoConsulta notificarCurtida(Usuario usuario, Curtida curtida, String tipo){
        TipoNotificacao tipoNotificacao = this.getTipoNotificacao("comunidade");
        String continuacaoTexto = "";
        Integer idReferencia = null;
        Usuario usuarioNotificado = usuario;

        if (tipo.equalsIgnoreCase("postagem")){
            continuacaoTexto = "sua publicação.";
            idReferencia = curtida.getPostagem().getId();
            usuarioNotificado = curtida.getPostagem().getUsuario();
        }else {
            continuacaoTexto = "seu comentário.";
            idReferencia = curtida.getComentario().getId();
            usuarioNotificado = curtida.getComentario().getUsuario();
        }

        String texto = "O %s curtiu %s".formatted(curtida.getUsuario().getNome(), continuacaoTexto);
        Notificacao novaNotificacao = notificacaoRepository.save(new Notificacao(usuario, tipoNotificacao, idReferencia, texto, usuarioNotificado));
        return new NotificacaoConsulta(novaNotificacao, "Alguém curtiu %s ".formatted(continuacaoTexto), idReferencia);
    }

    public NotificacaoConsulta notificarComentario(String token, Comentario comentario){
        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        Usuario usuarioNotificado = comentario.getPostagem().getUsuario();
        TipoNotificacao tipoNotificacao = this.getTipoNotificacao("comunidade");
        String texto = "%s comentou sua publicação".formatted(comentario.getUsuario().getNome());
        Integer idReferencia = comentario.getPostagem().getId();
        Notificacao novaNotificacao = notificacaoRepository.save(new Notificacao(usuario, tipoNotificacao, idReferencia, texto, usuarioNotificado));
        return new NotificacaoConsulta(novaNotificacao, "Houve um comentário na sua publicação.", idReferencia);
    }


    public void notificarUpdateAgendamento(String token, Agendamento agendamento){
        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        Usuario usuarioNotificado = global.getUsuarioByToken(token);
        TipoNotificacao tipoNotificacao = this.getTipoNotificacao("agendamento");
        String action = "";

        switch (agendamento.getStatus()){
            case "agendado":
                action = "confirmou";

            case "concluido":
                action = "concluiu";

            case "cancelado":
                action = "cancelou";
        }

        String texto = "";


        boolean clientSide = true;

        if (usuario.getDtype().equalsIgnoreCase("Barbeiro")) {
            Barbeiro barbeiro = barbeiroRepository.findById(usuario.getId()).get();
            if(agendamento.getBarbearia().getId().equals(barbeiro.getBarbearia().getId())){
                clientSide = false;
            }
        }

        if (clientSide){
            texto = "A barbearia %s %s um agendamento.".formatted(agendamento.getBarbearia().getNomeNegocio(), action);
            usuarioNotificado = usuarioRepository.findAllByBarbeariaId(agendamento.getBarbearia().getId()).get(0);
        }else{
            texto = "%s %s um agendamento.".formatted(agendamento.getCliente().getNome(), action);
            usuarioNotificado = agendamento.getCliente();
        }

        Integer idReferencia = agendamento.getId();
        Notificacao novaNotificacao = notificacaoRepository.save(new Notificacao(usuario, tipoNotificacao, idReferencia, texto, usuarioNotificado));
        new NotificacaoConsulta(novaNotificacao, "Seu agendamento foi %s".formatted(agendamento.getStatus()), idReferencia);
    }

    public void notificarNovoAgendamento(String token, Agendamento agendamento){
        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        TipoNotificacao tipoNotificacao = this.getTipoNotificacao("agendamento");
        String texto = "%s realizou um agendamento.".formatted(agendamento.getCliente().getNome());
        Integer idReferencia = agendamento.getId();
        Usuario usuarioNotificado = usuarioRepository.findAllByBarbeariaId(agendamento.getBarbearia().getId()).get(0);
        Notificacao novaNotificacao = notificacaoRepository.save(new Notificacao(usuario, tipoNotificacao, idReferencia, texto, usuarioNotificado));
        new NotificacaoConsulta(novaNotificacao, "Você tem um novo agendamento", idReferencia);
    }


    public List<NotificacaoConsulta> getAllNotificacoesByTipo(String token, String tipo){
        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        TipoNotificacao tipoNotificacao = this.getTipoNotificacao(tipo);
        List<Notificacao> notificacacoes = notificacaoRepository.findByUsuarioNotificadoIdAndTipoNotificacaoIdOrderByDataCriacaoDesc(usuario.getId(), tipoNotificacao.getId());
        if (notificacacoes.isEmpty()) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum notificacao encontrada");
        return toDto(notificacacoes);
    }

    public void updateAllNotificacoesParaLidas(String token, String tipo){
        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        TipoNotificacao tipoNotificacao = this.getTipoNotificacao(tipo);
        notificacaoRepository.marcarNotificacoesComoLidas(usuario.getId(), tipoNotificacao.getId());
    }

    public void updateNotificacoesParaLidas(String token, Integer idNotificacao){
        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        Notificacao notificacao = notificacaoRepository.findById(idNotificacao).orElseThrow(
                () -> new RecursoNaoEncontradoException("Notificação", idNotificacao));
        notificacao.setLida(true);
        notificacao.setId(idNotificacao);
        notificacaoRepository.save(notificacao);
    }

    public CountTipoNotificacoesConsultas getCountNotificacoes(String token){
        global.validarUsuarioExiste(token);
        Usuario usuario = global.getUsuarioByToken(token);
        List<TipoNotificacao> tipos = Arrays.asList(
                getTipoNotificacao("mensagem"),
                getTipoNotificacao("agendamento"),
                getTipoNotificacao("comunidade")
        );

        List<Integer> countTipos = new ArrayList<>();
        for (TipoNotificacao tipo : tipos){
            countTipos.add(notificacaoRepository.contarNotificacoesNaoLidas(usuario.getId(), tipo.getId()));
        }

        return new CountTipoNotificacoesConsultas(countTipos.get(0), countTipos.get(1), countTipos.get(2));
    }


    public TipoNotificacao getTipoNotificacao(String tipoNotificacao){
        return tipoNotificacaoRepository.existsByNome(tipoNotificacao) ? tipoNotificacaoRepository.findByNome(tipoNotificacao)
                : tipoNotificacaoRepository.save(new TipoNotificacao(tipoNotificacao));
    }

    public List<NotificacaoConsulta> toDto(List<Notificacao> notificacoes){

        List<NotificacaoConsulta> dtos = new ArrayList<>();

        for (Notificacao n : notificacoes){

            String tipoNotificacao = n.getTipoNotificacao().getNome();
            String titulo = "";

            switch (tipoNotificacao){
                case "mensagem":
                    titulo = "Você tem uma nova mensagem!";
                case "comunidade":
                    titulo = "Você ganhou um like!";
                case "agendamento":
                    titulo = "Há novidades em seus agendamentos";

            }

            dtos.add(new NotificacaoConsulta(n, titulo));

        }

        return dtos;
    }



}
