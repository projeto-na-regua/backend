package projetopi.projetopi.service;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.util.Token;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.http.ResponseEntity.ok;

@Service
public class UsuarioService {


    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public Token token;

    @Autowired
    public ModelMapper mapper;

    @Autowired
    private final AzureStorageService azureStorageService;

    public UsuarioService(AzureStorageService azureStorageService) {
        this.azureStorageService = azureStorageService;
    }


    // CADASTRO CLIENTE
    public TokenConsulta cadastrarCliente(CadastroCliente c){

        Integer idEdereco = enderecoRepository.save(c.gerarEndereco()).getId();
        Cliente cliente = c.gerarCliente();


        cliente.setEndereco(enderecoRepository.getReferenceById(idEdereco));
        clienteRepository.save(cliente);
        String tk = token.getToken(cliente);
        return new TokenConsulta( UsuarioMapper.toEntity(c), tk);
    }

    // CADASTRO BARBEIRO
    public TokenConsulta cadastrarBarbeiro(CadastroBarbearia nvBarbearia){

        Endereco endereco = nvBarbearia.gerarEndereco();
        Barbearia barbearia = nvBarbearia.gerarBarbearia();
        Barbeiro barbeiro = nvBarbearia.gerarBarbeiro();
        DiaSemana[] diaSemana = nvBarbearia.gerarSemena();
        String tk = token.getToken(barbeiro);

        Integer idEndereco = enderecoRepository.save(endereco).getId();

        barbearia.setEndereco(enderecoRepository.getReferenceById(idEndereco));

        Integer idBarbearia = barbeariasRepository.save(barbearia).getId();

        barbeiro.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));

        barbeiroRepository.save(barbeiro);

        for (DiaSemana d: diaSemana){
            d.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));
            diaSemanaRepository.save(d);
        }

        return new TokenConsulta(barbeiro, tk);
    }


    public InfoUsuario editarUsuario(Integer id, InfoUsuario nvUsuario){


        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            Cliente usuario = nvUsuario.gerarCliente();
            usuario.setId(clienteRepository.getReferenceById(id).getId());
            usuario.setSenha(usuarioRepository.getReferenceById(id).getSenha());
            usuarioRepository.save(usuario);
            return nvUsuario;

        }else{
            Barbeiro usuario = nvUsuario.gerarBarberiro();
            usuario.setId(usuarioRepository.getReferenceById(id).getId());
            usuario.setSenha(usuarioRepository.getReferenceById(id).getSenha());
            usuarioRepository.save(usuario);
            return nvUsuario;
        }
    }

    public Integer getUserId(String t){
       return Integer.valueOf(token.getUserIdByToken(t));
    }

    public boolean usuarioExistsById(Integer id){
        return usuarioRepository.existsById(id);
    }

    public boolean usuarioExistsByEmail(String email){
        return usuarioRepository.findByEmail(email) != null;
    }


    public TokenConsulta loginIsValid(LoginUsuario user){

        Usuario u = usuarioRepository.findByEmailAndSenha(user.getEmail(), user.getSenha());

        if (u != null){

            String tk = token.getToken(u);

            if (u instanceof Cliente) {
                return new TokenConsulta((Cliente) u, tk);
            } else if (u instanceof Barbeiro) {
                return new TokenConsulta((Barbeiro) u, tk);
            }
        }

        return null;
    }


    public List<InfoUsuario> getUsuario(String t){

        Integer id = Integer.valueOf(token.getUserIdByToken(t));

        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            return clienteRepository.findByInfoUsuario(id);

        }else{
            return barbeiroRepository.findByInfoUsuario(id);
        }

    }

    public boolean usuarioExiste(String token){
        return usuarioRepository.existsById(getUserId(token));
    }

    public ResponseEntity<ImgConsulta> editarImgPerfil(String t, MultipartFile file){
        try {
            String imageUrl = azureStorageService.uploadImage(file);
            Usuario usuario = usuarioRepository.findById(getUserId(t)).get();
            usuario.setImgPerfil(imageUrl);
            usuario.setId(getUserId(t));
            usuarioRepository.save(usuario);
            return ResponseEntity.ok().body(new ImgConsulta(usuarioRepository.findById(getUserId(t)).get().getImgPerfil()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ByteArrayResource> getImage(String t) {
        try {
            String imageName = usuarioRepository.findById(getUserId(t)).get().getImgPerfil();
            byte[] blobBytes = azureStorageService.getBlob(imageName);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(blobBytes));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(imageBytes.length)
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
