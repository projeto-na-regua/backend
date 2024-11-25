package projetopi.projetopi.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.GaleriaConsulta;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.ImgsGaleria;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.ClienteRepository;
import projetopi.projetopi.repository.GaleriaRepository;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

@Service
public class GaleriaService {
    @Autowired
    private GaleriaRepository galeriaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private Token token;
    @Autowired
    private Global global;
    @Autowired
    private ImageService imageService;

    public GaleriaService() {
    }

    public List<GaleriaConsulta> getImages(String tk) {
        this.global.validaCliente(tk, "Cliente");
        Integer id = Integer.valueOf(this.token.getUserIdByToken(tk));
        List<ImgsGaleria> images = this.galeriaRepository.findByClienteIdAndIsActiveTrueOrIsActiveIsNull(id);
        List<GaleriaConsulta> dtos = (List)images.stream().map((img) -> {
            return new GaleriaConsulta(img, this.imageService.getImgURL("galeria", img.getImagem()));
        }).collect(Collectors.toList());
        if (images.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        } else {
            return dtos;
        }
    }

    public GaleriaConsulta uploadImg(String tk, MultipartFile imagem, String descricao) {
        Integer id = Integer.valueOf(this.token.getUserIdByToken(tk));
        this.global.validaCliente(tk, "Cliente");
        String imageUrl = this.imageService.upload(imagem, "galeria");
        Cliente cliente = (Cliente)this.clienteRepository.findById(id).orElseThrow(() -> {
            return new RecursoNaoEncontradoException("Cliente", id);
        });
        ImgsGaleria imgsGaleria = (ImgsGaleria)this.galeriaRepository.save(new ImgsGaleria(imageUrl, descricao, cliente));
        return new GaleriaConsulta(imgsGaleria, this.imageService.getImgURL("galeria", imgsGaleria.getImagem()));
    }

    public GaleriaConsulta getOneImageGalery(String tk, Integer idImg) {
        this.global.validaCliente(tk, "Cliente");

        try {
            ImgsGaleria imgsGaleria = (ImgsGaleria)this.galeriaRepository.findById(idImg).orElseThrow(() -> {
                return new RecursoNaoEncontradoException("Imagem Gaelria", idImg);
            });
            return new GaleriaConsulta(imgsGaleria, this.imageService.getImgURL(imgsGaleria.getImagem(), "galeria"));
        } catch (Exception var4) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void delete(String tk, Integer idImg) {
        this.global.validaCliente(tk, "Cliente");

        try {
            ImgsGaleria imgsGaleria = (ImgsGaleria)this.galeriaRepository.findById(idImg).orElseThrow(() -> {
                return new RecursoNaoEncontradoException("Imagem Gaelria", idImg);
            });
            imgsGaleria.setIsActive(false);
            imgsGaleria.setId(imgsGaleria.getId());
            this.galeriaRepository.save(imgsGaleria);
        } catch (Exception var4) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
