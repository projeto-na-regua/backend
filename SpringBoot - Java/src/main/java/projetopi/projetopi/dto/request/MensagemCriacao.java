package projetopi.projetopi.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import projetopi.projetopi.entity.Chat;
import projetopi.projetopi.entity.Usuario;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensagemCriacao {
    private Integer idBarbearia;
    private String conteudo;
}
