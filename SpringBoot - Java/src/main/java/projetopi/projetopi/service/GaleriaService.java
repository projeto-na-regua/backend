package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
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

    private final StorageService azureStorageService;


    public GaleriaService(StorageService azureStorageService) {
        this.azureStorageService = azureStorageService;
    }

    public List<GaleriaConsulta> getImages(String tk){

        global.validaCliente(tk, "Cliente");
        Integer id = Integer.valueOf(token.getUserIdByToken(tk));

        List<ImgsGaleria> images = galeriaRepository.findByClienteId(id);

        List<GaleriaConsulta> dtos = images.stream()
                .map(img -> new GaleriaConsulta(img, img.getImagem()))
                .collect(Collectors.toList());

        if (images.isEmpty()) throw new ResponseStatusException(HttpStatusCode.valueOf(204));


        return dtos;
    }

    public GaleriaConsulta uploadImg(String tk, MultipartFile imagem, String descricao){

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        global.validaCliente(tk, "Cliente");

        try {
            String imageUrl = azureStorageService.uploadImage(imagem);
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
            ImgsGaleria imgsGaleria = galeriaRepository.save(new ImgsGaleria(imageUrl, descricao, cliente));
            return new GaleriaConsulta(imgsGaleria, imgsGaleria.getImagem());

        } catch (IOException e) {
            throw new ErroServidorException("upload de imagem.");

        }
    }

    public GaleriaConsulta getOneImageGalery(String tk, Integer idImg) {
        global.validaCliente(tk, "Cliente");
        try {

            ImgsGaleria imgsGaleria = galeriaRepository.findById(idImg).orElseThrow(() -> new RecursoNaoEncontradoException("Imagem Gaelria", idImg));
            return new GaleriaConsulta(imgsGaleria, azureStorageService.getBlobUrl(imgsGaleria.getImagem()));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
