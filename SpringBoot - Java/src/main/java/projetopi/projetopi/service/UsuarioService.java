package projetopi.projetopi.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.controller.UsuarioController;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.dto.response.*;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.exception.ConflitoException;
import projetopi.projetopi.exception.ErroServidorException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Dia;
import projetopi.projetopi.util.Global;
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
    private final Global global;
    private final Token token;
    public ModelMapper mapper;
    private final StorageService azureStorageService;


    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);


    public UsuarioService(BarbeiroRepository barbeiroRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, BarbeariasRepository barbeariasRepository, DiaSemanaRepository diaSemanaRepository, UsuarioRepository usuarioRepository, Token token, ModelMapper mapper, StorageService azureStorageService, Global global) {
        this.barbeiroRepository = barbeiroRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.barbeariasRepository = barbeariasRepository;
        this.diaSemanaRepository = diaSemanaRepository;
        this.usuarioRepository = usuarioRepository;
        this.token = token;
        this.mapper = mapper;
        this.azureStorageService = azureStorageService;
        this.global = global;
    }


    public String cadastrarCliente(CadastroCliente nvCliente){
        validarEmail(nvCliente.getEmail());

        Endereco endereco = cadastroEndereco(nvCliente);
        validarEnderecoCadastrado(endereco);

        Integer idEndereco = endereco.getId();
        Cliente cliente = UsuarioMapper.toDto(nvCliente);


        cliente.getEndereco().setId(idEndereco);
        clienteRepository.save(cliente);
        return token.getToken(cliente);
    }


    public Endereco cadastroEndereco(CadastroBarbearia nvBarbearia){
        Endereco endereco = nvBarbearia.gerarEndereco();
        setCoordenadas(endereco);
        return enderecoRepository.save(endereco);
    }

    public Endereco cadastroEndereco(CadastroCliente nvCliente){
        Endereco endereco = UsuarioMapper.toDtoEndereco(nvCliente);
        setCoordenadas(endereco);
        return enderecoRepository.save(endereco);
    }

    public Endereco updateEndereco(Endereco endereco, Integer id){
        if (!enderecoRepository.existsById(id)){
            throw  new RecursoNaoEncontradoException("Endereço", id);
        }
        setCoordenadas(endereco);
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

        DiaSemana seg = new DiaSemana(Dia.SEG);
        DiaSemana ter = new DiaSemana(Dia.TER);
        DiaSemana qua = new DiaSemana(Dia.QUA);
        DiaSemana qui = new DiaSemana(Dia.QUI);
        DiaSemana sex = new DiaSemana(Dia.SEX);
        DiaSemana sab = new DiaSemana(Dia.SAB);
        DiaSemana dom = new DiaSemana(Dia.DOM);


        DiaSemana[] diaSemanas = {seg, ter, qua, qui, sex, sab, dom};

        for (DiaSemana diaSemana : diaSemanas){
            diaSemana.setBarbearia(barbearia);
            diaSemanaRepository.save(diaSemana);
        }
        clienteRepository.atualizarClienteParaBarbeiro(id, barbearia, true);
        return barbearia;
    }

    public String getImagePerfilClienteSide(String tk) {
        global.validaCliente(tk, "Cliente");

        if (!usuarioRepository.existsById(Integer.valueOf(token.getUserIdByToken(tk)))){
            throw new RecursoNaoEncontradoException("Usuário", Integer.valueOf(token.getUserIdByToken(tk)));
        }

        String imageName = usuarioRepository.findById(Integer.valueOf(token.getUserIdByToken(tk))).get().getImgPerfil();

        return azureStorageService.getBlobUrl(imageName);
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
        PerfilUsuarioConsulta dto  = new PerfilUsuarioConsulta(usuarioRepository.findById(id).get());
        dto.setImgPerfil(azureStorageService.getBlobUrl(dto.getImgPerfil()));
        return dto;

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

    public Coordenada gerarCoordenadas(String postalcode){
        String url = String.format(
                "https://cep.awesomeapi.com.br/json/");


        RestClient client = RestClient.builder()
                .baseUrl(url)
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        Coordenada data = client.get()
                .uri(postalcode)
                .retrieve()
                .body(Coordenada.class);

        log.info("Resposta da API: " + data);
        return data;
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

    void setCoordenadas(Endereco endereco){
        Coordenada coordenada = gerarCoordenadas(endereco.getCep());
        endereco.setLongitude(Double.valueOf(coordenada.getLng()));
        endereco.setLatitude(Double.valueOf(coordenada.getLat()));
    }

}
