package projetopi.projetopi.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.DtypeConsulta;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.dto.response.TokenConsulta;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.util.Token;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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
    public String cadastrarCliente(CadastroCliente c){

        Integer idEdereco = enderecoRepository.save(c.gerarEndereco()).getId();
        Cliente cliente = c.gerarCliente();


        cliente.setEndereco(enderecoRepository.getReferenceById(idEdereco));
        clienteRepository.save(cliente);
        String tk = token.getToken(cliente);
        return tk;
    }

    // CADASTRO BARBEIRO
    public String cadastrarBarbeiro(CadastroBarbearia nvBarbearia){

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

        return tk;
    }


    public UsuarioConsulta editarUsuario(Integer id, UsuarioConsulta nvUsuario){


        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            Cliente usuario = mapper.map(nvUsuario, Cliente.class);
            usuario.setId(clienteRepository.getReferenceById(id).getId());
            usuario.setSenha(usuarioRepository.getReferenceById(id).getSenha());
            usuarioRepository.save(usuario);
            return nvUsuario;

        }else{
            Barbeiro usuario = mapper.map(nvUsuario, Barbeiro.class);
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


    public String loginIsValid(LoginUsuario user){

        Usuario u = usuarioRepository.findByEmailAndSenha(user.getEmail(), user.getSenha());

        if (u != null){

            String tk = token.getToken(u);
            return tk;
        }

        return null;
    }


    public UsuarioConsulta getPerfil(String t){

        Integer id = Integer.valueOf(token.getUserIdByToken(t));

        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            return clienteRepository.findByInfoUsuario(id).get(0);

        }else{
            return barbeiroRepository.findByInfoUsuario(id).get(0);
        }

    }

    public DtypeConsulta getUsuario(String t){

        Integer id = Integer.valueOf(token.getUserIdByToken(t));

        return mapper.map(usuarioRepository.findById(id), DtypeConsulta.class);

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
