package projetopi.projetopi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErroServidorException extends RuntimeException{

    public ErroServidorException(String message) {
        super("Erro interno no servidor ao fazer " + message);
    }
}
