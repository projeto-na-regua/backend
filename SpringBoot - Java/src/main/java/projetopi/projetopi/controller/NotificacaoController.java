package projetopi.projetopi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dto.response.CountTipoNotificacoesConsultas;
import projetopi.projetopi.dto.response.NotificacaoConsulta;
import projetopi.projetopi.service.NotificacoesService;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacoesService notificacaoService;

    @Operation(summary = "Buscar notificações por tipo",
            description = "Retorna todas as notificações de um usuário, filtradas pelo tipo especificado e ordenadas por data de criação em ordem decrescente.")
    @GetMapping("/buscar")
    public ResponseEntity<List<NotificacaoConsulta>> getAllNotificacoesByTipo(
            @Parameter(description = "Token de autenticação do usuário", required = true)
            @RequestHeader("Authorization") String token,

            @Parameter(description = "Tipo de notificação a ser filtrado. Valores permitidos: 'mensagem', 'comunidade', 'agendamento'",
                    required = true, example = "agendamento")
            @RequestParam String tipo) {

        List<NotificacaoConsulta> notificacoes = notificacaoService.getAllNotificacoesByTipo(token, tipo);
        return new ResponseEntity<>(notificacoes, HttpStatus.OK);
    }

    @Operation(summary = "Marcar todas as notificações como lidas por tipo",
            description = "Marca todas as notificações de um usuário, de um tipo específico, como lidas.")
    @PutMapping("/marcarTodasLidas")
    public ResponseEntity<Void> updateAllNotificacoesParaLidas(
            @Parameter(description = "Token de autenticação do usuário", required = true)
            @RequestHeader("Authorization") String token,

            @Parameter(description = "Tipo de notificação a ser marcada como lida. Valores permitidos: 'mensagem', 'comunidade', 'agendamento'",
                    required = true, example = "comunidade")
            @RequestParam String tipo) {

        notificacaoService.updateAllNotificacoesParaLidas(token, tipo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Marcar uma notificação específica como lida",
            description = "Marca uma notificação específica como lida com base no ID da notificação.")
    @PutMapping("/marcarLida")
    public ResponseEntity<Void> updateNotificacaoParaLida(
            @Parameter(description = "Token de autenticação do usuário", required = true)
            @RequestHeader("Authorization") String token,

            @Parameter(description = "ID da notificação a ser marcada como lida", required = true)
            @RequestParam Integer idNotificacao) {

        notificacaoService.updateNotificacoesParaLidas(token, idNotificacao);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obter contagem de notificações não lidas por tipo",
            description = "Retorna a contagem de notificações não lidas para cada tipo: 'mensagem', 'agendamento' e 'comunidade'.")
    @GetMapping("/contarNaoLidas")
    public ResponseEntity<CountTipoNotificacoesConsultas> getCountNotificacoes(
            @Parameter(description = "Token de autenticação do usuário", required = true)
            @RequestHeader("Authorization") String token) {

        CountTipoNotificacoesConsultas count = notificacaoService.getCountNotificacoes(token);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
