package projetopi.projetopi.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


@Getter
public class GaleriaCriacao {
    private MultipartFile imagem;
    private String descricao;
}
