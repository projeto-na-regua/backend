package projetopi.projetopi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends RuntimeException{

    public RecursoNaoEncontradoException(String recurso, Object identificador) {
        super("%s de idit entificador %s não encontrado".formatted(recurso, identificador));
    }

}
