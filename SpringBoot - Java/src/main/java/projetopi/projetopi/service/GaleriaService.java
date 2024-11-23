package projetopi.projetopi.service;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.GaleriaConsulta;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.ImgsGaleria;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.ErroServidorException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.ClienteRepository;
import projetopi.projetopi.repository.GaleriaRepository;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GaleriaService {

    @Autowired
    private GaleriaRepository galeriaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private  Token token;

    @Autowired
    private Global global;

    @Autowired
    private ImageService imageService;




    public List<GaleriaConsulta> getImages(String tk){

        global.validaCliente(tk, "Cliente");
        Integer id = Integer.valueOf(token.getUserIdByToken(tk));

        List<ImgsGaleria> images = galeriaRepository.findByClienteIdAndIsActiveTrueOrIsActiveIsNull(id);

        List<GaleriaConsulta> dtos = images.stream()
                .map(img -> new GaleriaConsulta(img, imageService.getImgURL("galeria", img.getImagem())))
                .collect(Collectors.toList());

        if (images.isEmpty()) throw new ResponseStatusException(HttpStatusCode.valueOf(204));


        return dtos;
    }

    public GaleriaConsulta uploadImg(String tk, MultipartFile imagem, String descricao){

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        global.validaCliente(tk, "Cliente");

        String imageUrl = imageService.upload(imagem, "galeria");
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
        ImgsGaleria imgsGaleria = galeriaRepository.save(new ImgsGaleria(imageUrl, descricao, cliente));
        return new GaleriaConsulta(imgsGaleria,imageService.getImgURL("galeria", imgsGaleria.getImagem()));

    }

    public GaleriaConsulta getOneImageGalery(String tk, Integer idImg) {
        global.validaCliente(tk, "Cliente");
        try {

            ImgsGaleria imgsGaleria = galeriaRepository.findById(idImg).orElseThrow(() -> new RecursoNaoEncontradoException("Imagem Gaelria", idImg));
            return new GaleriaConsulta(imgsGaleria, imageService.getImgURL(imgsGaleria.getImagem(), "galeria"));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void delete(String tk, Integer idImg) {
        global.validaCliente(tk, "Cliente");

        try {

            ImgsGaleria imgsGaleria = galeriaRepository.findById(idImg).orElseThrow(() -> new RecursoNaoEncontradoException("Imagem Gaelria", idImg));
            imgsGaleria.setIsActive(false);
            imgsGaleria.setId(imgsGaleria.getId());
            galeriaRepository.save(imgsGaleria);


        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
