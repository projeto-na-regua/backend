package projetopi.projetopi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflitoException extends RuntimeException{

    public ConflitoException(String recurso, Object atributo) {
        super("Esse %s já existe, há um conflito com o %s".formatted(recurso, atributo));
    }
}
