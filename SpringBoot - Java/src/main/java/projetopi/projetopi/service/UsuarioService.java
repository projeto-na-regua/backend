package projetopi.projetopi.service;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.dto.request.EditarSenha;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.DtypeConsulta;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.dto.response.PerfilUsuarioConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.exception.ConflitoException;
import projetopi.projetopi.exception.ErroServidorException;
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

    private final BarbeiroRepository barbeiroRepository;

    private final ClienteRepository clienteRepository;

    private final EnderecoRepository enderecoRepository;

    private final BarbeariasRepository barbeariasRepository;

    private final DiaSemanaRepository diaSemanaRepository;

    private final UsuarioRepository usuarioRepository;

    private final Token token;

    public ModelMapper mapper;

    private final StorageService azureStorageService;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(BarbeiroRepository barbeiroRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, BarbeariasRepository barbeariasRepository, DiaSemanaRepository diaSemanaRepository, UsuarioRepository usuarioRepository, Token token, ModelMapper mapper, StorageService azureStorageService, PasswordEncoder passwordEncoder) {
        this.barbeiroRepository = barbeiroRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.barbeariasRepository = barbeariasRepository;
        this.diaSemanaRepository = diaSemanaRepository;
        this.usuarioRepository = usuarioRepository;
        this.token = token;
        this.mapper = mapper;
        this.azureStorageService = azureStorageService;
        this.passwordEncoder =  passwordEncoder;
    }


    public String cadastrarCliente(CadastroCliente nvCliente){
        validarEmail(nvCliente.getEmail());

        Endereco endereco = enderecoRepository.save(UsuarioMapper.toDtoEndereco(nvCliente));
        validarEnderecoCadastrado(endereco);

        Integer idEndereco = endereco.getId();
        Cliente cliente = UsuarioMapper.toDto(nvCliente);

        String senhaCriptografada = passwordEncoder.encode(nvCliente.getSenha());
        cliente.setSenha(senhaCriptografada);
        cliente.getEndereco().setId(idEndereco);
        clienteRepository.save(cliente);
        return token.getToken(cliente);
    }


    public Endereco cadastroEndereco(CadastroBarbearia nvBarbearia){
        Endereco endereco = nvBarbearia.gerarEndereco();
        return enderecoRepository.save(endereco);
    }

    public Barbearia cadastroBarbearia(CadastroBarbearia nvBarbearia, Endereco endereco){
        Barbearia barbearia = nvBarbearia.gerarBarbearia();

        if (!enderecoRepository.existsById(endereco.getId())){
            throw new RecursoNaoEncontradoException("Endereco", endereco);
        }

        barbearia.setEndereco(enderecoRepository.getReferenceById(endereco.getId()));
        return barbeariasRepository.save(barbearia);
    }

    public Barbearia cadastrarBarbeariaByDto(CadastroBarbearia nvBarbearia, String tk){
        validarCpf(nvBarbearia.getCpf());
        validarSeUsuarioPossuiBarbearia(tk);
        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        Endereco endereco = cadastroEndereco(nvBarbearia);
        Barbearia barbearia = cadastroBarbearia(nvBarbearia, endereco);
        clienteRepository.atualizarClienteParaBarbeiro(id, barbearia, true);
        return barbearia;
    }


    public UsuarioConsulta editarUsuario(String tk, UsuarioConsulta nvUsuario){

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        validarUsuarioExiste(id);

        if(usuarioRepository.getDtypeById(id).equals("Cliente")){
            Cliente usuario = mapper.map(nvUsuario, Cliente.class);
            Cliente clienteSalvo = clienteRepository.findById(id).get();

            usuario.setId(id);
            usuario.setSenha(clienteSalvo.getSenha());
            usuario.setEndereco(clienteSalvo.getEndereco());
            clienteRepository.save(usuario);
            return mapper.map(usuario, UsuarioConsulta.class);

        }else{
            Barbeiro usuario = mapper.map(nvUsuario, Barbeiro.class);
            Barbeiro barbeiroSalvo = barbeiroRepository.findById(id).get();


            usuario.setId(barbeiroSalvo.getId());
            usuario.setSenha(barbeiroSalvo.getSenha());
            usuario.setAdm(barbeiroSalvo.isAdm());
            usuario.setBarbearia(barbeiroSalvo.getBarbearia());
            usuario.setEndereco(barbeiroSalvo.getEndereco());
            barbeiroRepository.save(usuario);
            return mapper.map(usuario, UsuarioConsulta.class);
        }
    }



    public String loginIsValid(LoginUsuario user){
        Usuario u = usuarioRepository.findByEmailAndSenha(user.getEmail(), user.getSenha());

        if (u == null){
            throw new RecursoNaoEncontradoException("Usuário", user);
        }
        String tk = token.getToken(u);
        validarToken(tk);
        return tk;
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


    public ImgConsulta editarImgPerfil(String tk, MultipartFile file){

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        validarUsuarioExiste(id);

        try {
            String imageUrl = azureStorageService.uploadImage(file);
            Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
            usuario.setImgPerfil(imageUrl);
            usuarioRepository.save(usuario);
            return new ImgConsulta(usuario.getImgPerfil());

        } catch (IOException e) {
            throw new ErroServidorException("upload de imagem.");

        }
    }

    public ByteArrayResource getImage(String tk) {
        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        validarUsuarioExiste(id);
        try {
            String imageName = usuarioRepository.findById(id).get().getImgPerfil();
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
                }
            }
        }
    }

    void validarUsuarioExiste(Integer id){
         if (!usuarioRepository.existsById(id)){
             throw new RecursoNaoEncontradoException("Usuário", id);
         }
    }

    void validarEnderecoCadastrado(Endereco endereco){
        if (endereco == null || endereco.getId() == null) {
            throw new RecursoNaoEncontradoException("Endereco", endereco);
        }
    }

}
