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
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.DtypeConsulta;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.dto.response.PerfilUsuarioConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.exception.ConflitoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
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
    private final StorageService azureStorageService;

    public UsuarioService(StorageService azureStorageService) {
        this.azureStorageService = azureStorageService;
    }



    public String cadastrarCliente(CadastroCliente nvCliente){
        validarEmail(nvCliente.getEmail());
        Integer idEdereco = enderecoRepository.save(UsuarioMapper.toDtoEndereco(nvCliente)).getId();
        Cliente cliente = UsuarioMapper.toDto(nvCliente);
        cliente.getEndereco().setId(idEdereco);
        clienteRepository.save(cliente);
        return token.getToken(cliente);
    }




    // CADASTRO BARBEIRO
    public Barbearia cadastrarBarbeiro(CadastroBarbearia nvBarbearia, String tk){
        validarCpf(nvBarbearia.getCpf());
        validarSeUsuarioPossuiBarbearia(tk);

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));

        Endereco endereco = nvBarbearia.gerarEndereco();
        Barbearia barbearia = nvBarbearia.gerarBarbearia();
        DiaSemana[] diaSemana = nvBarbearia.gerarSemena();

        Integer idEndereco = enderecoRepository.save(endereco).getId();

        barbearia.setEndereco(enderecoRepository.getReferenceById(idEndereco));

        Integer idBarbearia = barbeariasRepository.save(barbearia).getId();

        clienteRepository.atualizarClienteParaBarbeiro(id, barbearia, true);


        for (DiaSemana d: diaSemana){
            d.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));
            diaSemanaRepository.save(d);
        }

        return barbearia;

    }




    public UsuarioConsulta editarUsuario(String tk, UsuarioConsulta nvUsuario){

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        validarUsuarioExiste(id);

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



    public String loginIsValid(LoginUsuario user){
        Usuario u = usuarioRepository.findByEmailAndSenha(user.getEmail(), user.getSenha());
        String tk = token.getToken(u);
        validarToken(tk);
        return token.getToken(u);
    }


    public PerfilUsuarioConsulta getPerfil(String t){
        Integer id = Integer.valueOf(token.getUserIdByToken(t));
        validarUsuarioExiste(id);
        return new PerfilUsuarioConsulta(usuarioRepository.findById(id).get());

    }

    public DtypeConsulta getUsuario(String t){
        Integer id = Integer.valueOf(token.getUserIdByToken(t));
        validarUsuarioExiste(id);
        return mapper.map(usuarioRepository.findById(id), DtypeConsulta.class);
    }


    public ResponseEntity<ImgConsulta> editarImgPerfil(String tk, MultipartFile file){

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        validarUsuarioExiste(id);

        try {
            String imageUrl = azureStorageService.uploadImage(file);
            Usuario usuario = usuarioRepository.findById(id).get();
            usuario.setImgPerfil(imageUrl);
            usuario.setId(id);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok().body(new ImgConsulta(usuarioRepository.findById(id).get().getImgPerfil()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ByteArrayResource> getImage(String tk) {
        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        validarUsuarioExiste(id);
        try {
            String imageName = usuarioRepository.findById(id).get().getImgPerfil();
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

    void validarEmail(String email){
        if (usuarioRepository.findByEmail(email) != null){
            throw new ConflitoException("Usuário", email);
        }
    }

    void validarToken(String token){
        if (token == null) throw new RecursoNaoEncontradoException("Usuário", token);
    }


    void validarCpf(String cpf){
        if (barbeariasRepository.findByCpf(cpf) != null){
            throw new ConflitoException("Usuário", cpf);
        }
    }


    void validarSeUsuarioPossuiBarbearia(String tk){
        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        Usuario u = usuarioRepository.findById(id).get();
        List<Barbeiro> barbeiros = barbeiroRepository.findAll();
        for (Barbeiro b: barbeiros){
            if(b.getId() == u.getId()){
                if(b.getBarbearia() != null){
                    throw new ConflitoException("Barbearia", u);
                };
            }
        }
    }

    void validarUsuarioExiste(Integer id){
         if (usuarioRepository.existsById(id)){
             throw new RecursoNaoEncontradoException("Usuário", id);
         };
    }



}
