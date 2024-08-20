package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.request.GaleriaCriacao;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.ImgsGaleria;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.ErroServidorException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.ClienteRepository;
import projetopi.projetopi.repository.GaleriaRepository;
import projetopi.projetopi.repository.UsuarioRepository;
import projetopi.projetopi.util.Token;

import java.io.IOException;
import java.util.List;

@Service
public class GaleriaService {

    @Autowired
    private GaleriaRepository galeriaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private  Token token;

    private final StorageService azureStorageService;


    public GaleriaService(StorageService azureStorageService) {
        this.azureStorageService = azureStorageService;
    }

    public List<ImgsGaleria> getImages(){

        List<ImgsGaleria> images = galeriaRepository.findAll();

        if (images.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return images;
    }

    public ImgsGaleria uploadImg(String tk, GaleriaCriacao galeriaCriacao){

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));

        try {
            String imageUrl = azureStorageService.uploadImage(galeriaCriacao.getImagem());
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
           return galeriaRepository.save(new ImgsGaleria(imageUrl, galeriaCriacao.getDescricao(), cliente));

        } catch (IOException e) {
            throw new ErroServidorException("upload de imagem.");

        }
    }


}
