package projetopi.projetopi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.entity.ImgsGaleria;


@Getter
public class GaleriaConsulta {
    private Integer id;
    private String linkImagem;
    private String descricao;

    public GaleriaConsulta(ImgsGaleria imgsGaleria, String linkImagem) {
        this.id = imgsGaleria.getId();
        this.linkImagem = linkImagem;
        this.descricao = imgsGaleria.getDescricao();
    }
}
