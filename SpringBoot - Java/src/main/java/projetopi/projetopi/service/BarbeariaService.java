package projetopi.projetopi.service;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.*;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.exception.ErroServidorException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.ClienteRepository;
import projetopi.projetopi.repository.DiaSemanaRepository;
import projetopi.projetopi.repository.EnderecoRepository;
import projetopi.projetopi.util.Dia;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.ResponseEntity.status;

@Service
@RequiredArgsConstructor
public class BarbeariaService {

    @Autowired
    private final BarbeariasRepository barbeariasRepository;

    @Autowired
    private final DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private final EnderecoRepository enderecoRepository;

    private final StorageService azureStorageService;

    private final Global global;

    private final ModelMapper mapper;

    private final Token tk;

    @Autowired
    private EnderecoService enderecoService;

    private final AgendamentoService agendamentoService;

    @Autowired
    private UsuarioService usuarioService;


//
//    public static double calcularDistancia(Point point, Double raio) {
//
//    }

    public List<Barbearia> getBarbeariaByEndereco(String token, double raio){
        global.validaCliente(token, "Cliente");
        Cliente cliente = clienteRepository.findById(Integer.valueOf(tk.getUserIdByToken(token))).get();

        List<Barbearia> barbearias = barbeariasRepository.findAll();
        List<Barbearia> barbeariasProximas = new ArrayList<>();


        for (Barbearia b : barbearias){
            barbeariasProximas.add(b);
        }


        return barbeariasProximas;
    }

    public List<BarbeariaPesquisa> findAll(String token){
        global.validaCliente(token, "Cliente");
        Cliente cliente = clienteRepository.findById(Integer.valueOf(tk.getUserIdByToken(token))).get();
        List<Barbearia> barbearias = barbeariasRepository.findAll();
        List<BarbeariaPesquisa> barbeariasProximas = new ArrayList<>();


//        for (Barbearia b : barbearias){
//            barbeariasProximas.add(new BarbeariaPesquisa(b));
//        }


        return barbeariasProximas;
    }


    public BarbeariaConsulta getPerfilForCliente(String token, Integer barbeariaId){
        global.validaCliente(token, "Cliente");

        if (!barbeariasRepository.existsById(barbeariaId)){
            throw new RecursoNaoEncontradoException("Barbearia", barbeariaId);
        }

        Barbearia barbearia = barbeariasRepository.findById(barbeariaId).get();
        DiaSemana[] semana = diaSemanaRepository.findByBarbeariaId(barbearia.getId());
        String banner = azureStorageService.getBlobUrl(barbearia.getImgBanner());
        String imgPerfil = azureStorageService.getBlobUrl(barbearia.getImgPerfil());
        return new BarbeariaConsulta(barbearia, semana, banner, imgPerfil);
    }

    public BarbeariaConsulta getPerfil(String token){
        global.validaBarbearia(token);
        Barbearia barbearia = barbeariasRepository.findById(global.getBarbeariaByToken(token).getId()).get();
        DiaSemana[] semana = diaSemanaRepository.findByBarbeariaId(barbearia.getId());
        String banner = azureStorageService.getBlobUrl(barbearia.getImgBanner());
        String imgPerfil = azureStorageService.getBlobUrl(barbearia.getImgPerfil());
        return new BarbeariaConsulta(barbearia, semana, banner, imgPerfil);
    }


    public BarbeariaConsulta editarPerfilInfo(String token, BarbeariaConsulta nvBarbearia){

        // Valida o token e obtém a barbearia associada
        global.validaBarbearia(token);
        Barbearia barbeariaExistente = global.getBarbeariaByToken(token);

        // Atualiza as informações do endereço
        Endereco enderecoAtualizado = new Endereco(nvBarbearia);
        enderecoService.updateEndereco(enderecoAtualizado, barbeariaExistente.getEndereco().getId());
        barbeariaExistente.setEndereco(enderecoAtualizado);

        // Atualiza as informações da barbearia existente
        barbeariaExistente.setNomeNegocio(nvBarbearia.getNomeNegocio());
        barbeariaExistente.setCelularNegocio(nvBarbearia.getCelularNegocio());
        barbeariaExistente.setEmailNegocio(nvBarbearia.getEmailNegocio());
        barbeariaExistente.setImgBanner(nvBarbearia.getImgBanner());
        barbeariaExistente.setImgPerfil(nvBarbearia.getImgPerfil());

        // Atualiza os dias da semana associados à barbearia
        DiaSemana[] semanaExistente = diaSemanaRepository.findByBarbeariaId(barbeariaExistente.getId());

        if (semanaExistente.length == 0){
            semanaExistente  = usuarioService.definirDiasDaSemanda();

            for (DiaSemana diaSemana : semanaExistente){
                diaSemana.setBarbearia(barbeariaExistente);
                diaSemanaRepository.save(diaSemana);
            }
        }

        for (int i = 0; i < semanaExistente.length; i++) {
            nvBarbearia.getDiaSemanas()[i].setId(semanaExistente[i].getId());
            nvBarbearia.getDiaSemanas()[i].setBarbearia(barbeariaExistente);
            diaSemanaRepository.save(nvBarbearia.getDiaSemanas()[i]);
        }

        // Atualiza a barbearia no repositório
        Barbearia barbeariaSalva = barbeariasRepository.save(barbeariaExistente);

        // Obtem as URLs das imagens atualizadas
        String bannerUrl = azureStorageService.getBlobUrl(barbeariaSalva.getImgBanner());
        String imgPerfilUrl = azureStorageService.getBlobUrl(barbeariaSalva.getImgPerfil());

        // Retorna as informações atualizadas
        return new BarbeariaConsulta(barbeariaSalva, nvBarbearia.getDiaSemanas(), bannerUrl, imgPerfilUrl);
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

    public List<String> getImagePerfilCliente(String token) {
        global.validaCliente(token, "Cliente");
        try {
            List<String> imageUrlList = new ArrayList<>();
            for (Barbearia barbearia : barbeariasRepository.findAll()) {
                String imageUrl = azureStorageService.getBlobUrl(barbearia.getImgPerfil());
                imageUrlList.add(imageUrl);
            }
            return imageUrlList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
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


    public String getImageBannerClieteSide(String token, Integer idBarbearia) {
        global.validaCliente(token, "Cliente");

        if (!barbeariasRepository.existsById(idBarbearia)){
            throw new RecursoNaoEncontradoException("Barbearia", idBarbearia);
        }

        String imageName = barbeariasRepository.findById(idBarbearia).get().getImgBanner();
//            byte[] blobBytes = azureStorageService.getBlob(imageName);
//
//            BufferedImage image = ImageIO.read(new ByteArrayInputStream(blobBytes));
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", baos);
//            byte[] imageBytes = baos.toByteArray();
//
//            ByteArrayResource resource = new ByteArrayResource(imageBytes);
        return azureStorageService.getBlobUrl(imageName);


    }

    public String getImagePerfilClienteSide(String token, Integer idBarbearia) {
        global.validaCliente(token, "Cliente");

        if (!barbeariasRepository.existsById(idBarbearia)){
            throw new RecursoNaoEncontradoException("Barbearia", idBarbearia);
        }

        String imageName = barbeariasRepository.findById(idBarbearia).get().getImgPerfil();
        return azureStorageService.getBlobUrl(imageName);
    }


    void validacoesPermissoes(String token){
        global.validaBarbeiro(token, "Servico");
        global.validarBarbeiroAdm(token, "Servico");
        global.validaBarbearia(token);
    }

}
