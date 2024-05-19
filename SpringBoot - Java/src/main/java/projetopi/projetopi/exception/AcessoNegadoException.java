package projetopi.projetopi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AcessoNegadoException extends RuntimeException{
    public AcessoNegadoException(String recurso) {
        super("Acesso negado ao recurso: %s".formatted(recurso));
    }
}
