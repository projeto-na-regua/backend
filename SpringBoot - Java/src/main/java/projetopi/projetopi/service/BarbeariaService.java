package projetopi.projetopi.service;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.BarbeariaConsulta;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.DiaSemana;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.ErroServidorException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.DiaSemanaRepository;
import projetopi.projetopi.repository.EnderecoRepository;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.springframework.http.ResponseEntity.status;

@Service
@RequiredArgsConstructor
public class BarbeariaService {

    @Autowired
    private final BarbeariasRepository barbeariasRepository;

    @Autowired
    private final DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private final EnderecoRepository enderecoRepository;

    private final StorageService azureStorageService;

    private final Global global;

    private final ModelMapper mapper;

    private final Token tk;

    public BarbeariaConsulta getPerfil(String token){
        global.validaBarbearia(token);
        Barbearia barbearia = barbeariasRepository.findById(global.getBarbeariaByToken(token).getId()).get();
        DiaSemana[] semana = diaSemanaRepository.findByBarbeariaId(barbearia.getId());
        return new BarbeariaConsulta(barbearia, semana);
    }


    public BarbeariaConsulta editarPerfilInfo(String token, BarbeariaConsulta nvBarbearia){

        global.validaBarbearia(token);
        Barbearia barbearia = barbeariasRepository.findById(global.getBarbeariaByToken(token).getId()).get();
        DiaSemana[] semana = diaSemanaRepository.findByBarbeariaId(barbearia.getId());

        Barbearia b = new Barbearia(nvBarbearia);
        Endereco endereco = new Endereco(nvBarbearia);

        endereco.setId(barbearia.getEndereco().getId());
        b.setId(barbearia.getId());
        b.setEndereco(enderecoRepository.save(endereco));


        for (int i = 0; i < semana.length; i++) {
            nvBarbearia.getDiaSemanas()[i].setId(semana[i].getId());
            nvBarbearia.getDiaSemanas()[i].setBarbearia(barbearia);
            diaSemanaRepository.save(nvBarbearia.getDiaSemanas()[i]);
        }

        return new BarbeariaConsulta(barbeariasRepository.save(b), nvBarbearia.getDiaSemanas());
    }

    public ImgConsulta editarImgPerfil(String token, MultipartFile file){

        validacoesPermissoes(token);
        try {
            String imageUrl = azureStorageService.uploadImage(file);
            Barbearia barbearia = barbeariasRepository.findById(global.getBarbeariaByToken(token).getId()).get();
            barbearia.setImgPerfil(imageUrl);
            barbeariasRepository.save(barbearia);
            return new ImgConsulta(barbearia.getImgPerfil());

        } catch (IOException e) {
            throw new ErroServidorException("upload de imagem.");

        }
    }

    public ByteArrayResource getImagePerfil(String token) {
        validacoesPermissoes(token);
        try {
            String imageName = barbeariasRepository.findById(global.getBarbeariaByToken(token).getId()).get().getImgPerfil();
            byte[] blobBytes = azureStorageService.getBlob(imageName);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(blobBytes));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            return resource;


        } catch (IOException e) {
            throw new ErroServidorException("ao resgatar imagem");
        }
    }

    public ImgConsulta editarImgBanner(String token, MultipartFile file){

        validacoesPermissoes(token);
        try {
            String imageUrl = azureStorageService.uploadImage(file);
            Barbearia barbearia = barbeariasRepository.findById(global.getBarbeariaByToken(token).getId()).get();
            barbearia.setImgBanner(imageUrl);
            barbeariasRepository.save(barbearia);
            return new ImgConsulta(barbearia.getImgBanner());

        } catch (IOException e) {
            throw new ErroServidorException("upload de imagem.");

        }
    }

    public ByteArrayResource getImageBanner(String token) {
        validacoesPermissoes(token);
        try {
            String imageName = barbeariasRepository.findById(global.getBarbeariaByToken(token).getId()).get().getImgBanner();
            byte[] blobBytes = azureStorageService.getBlob(imageName);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(blobBytes));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            return resource;


        } catch (IOException e) {
            throw new ErroServidorException("ao resgatar imagem");
        }
    }

    void validacoesPermissoes(String token){
        global.validaBarbeiro(token, "Servico");
        global.validarBarbeiroAdm(token, "Servico");
        global.validaBarbearia(token);
    }



}
