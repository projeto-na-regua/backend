package projetopi.projetopi.service;


import org.hibernate.id.IntegralDataTypeHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.controller.UsuarioController;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.EditarSenha;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.DtypeConsulta;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.dto.response.PerfilUsuarioConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.entity.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UsuarioServiceTest {

    UsuarioService service;

    BarbeiroRepository barbeiroRepository;

    ClienteRepository clienteRepository;

    EnderecoRepository enderecoRepository;

    BarbeariasRepository barbeariasRepository;

    DiaSemanaRepository diaSemanaRepository;

    UsuarioRepository usuarioRepository;

    Token token;

    ModelMapper mapper;

    StorageService azureStorageService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void iniciar(){
        barbeiroRepository = mock(BarbeiroRepository.class);
        clienteRepository = mock(ClienteRepository.class);
        enderecoRepository = mock(EnderecoRepository.class);
        barbeariasRepository = mock(BarbeariasRepository.class);
        diaSemanaRepository = mock(DiaSemanaRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        token = mock(Token.class);
        passwordEncoder = mock(PasswordEncoder.class);
        mapper = new ModelMapper();
        azureStorageService = mock(StorageService.class);
        service = new UsuarioService(barbeiroRepository,
                                    clienteRepository,
                                    enderecoRepository,
                                    barbeariasRepository,
                                    diaSemanaRepository,
                                    usuarioRepository,
                                    token,
                                    mapper,
                                    azureStorageService, passwordEncoder);

    }



    @DisplayName("Se o email existir, deve lançar exception. E se não existir, nada acontece.")
    @Test
    public void validarEmailExistente(){

        // Caso em que o email já existe
        String emailExistente = "teste@email.com";
        Cliente usuarioExistente = new Cliente();
        usuarioExistente.setEmail(emailExistente);
        when(usuarioRepository.findByEmail(emailExistente)).thenReturn(usuarioExistente);
        assertThrows(ConflitoException.class, () -> service.validarEmail(emailExistente));


        // Caso em que o email não existe
        String emailNaoExistente = "email_nao_existente@example.com";
        when(usuarioRepository.findByEmail(emailNaoExistente)).thenReturn(null);
        assertDoesNotThrow(() -> service.validarEmail(emailNaoExistente));
    }


    @DisplayName("Se o usuário existir, nada acontece. E se não existir, lanca exception.")
    @Test
    public void validarUsuarioExistente(){
        // Caso em que o usuário existe
        Integer idExistente = 2000;
        when(usuarioRepository.existsById(idExistente)).thenReturn(true);
        assertDoesNotThrow(() -> service.validarUsuarioExiste(idExistente));

        // Caso em que o usuário não existe
        Integer idInexistente = 9292;
        when(usuarioRepository.existsById(idInexistente)).thenReturn(false);
        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
                () -> service.validarUsuarioExiste(idInexistente));
        assertTrue(exception.getMessage().contains(String.valueOf(idInexistente)));
        assertThrows(RecursoNaoEncontradoException.class, () -> service.validarUsuarioExiste(idInexistente));
    }

    @DisplayName("Se o cpf existir, lanca exception.")
    @Test
    public void validarCpfExistente(){
        // Caso em que o cpf existe
        String cpf = "cpf";
        Barbearia barbearia = new Barbearia();
        barbearia.setCpf(cpf);
        when(barbeariasRepository.findByCpf(cpf)).thenReturn(barbearia);
        assertThrows(ConflitoException.class, () -> service.validarCpf(cpf));

        // Caso em que o cpf não existe
        String cpfInexistente = "não existe";
        when(barbeariasRepository.findByCpf(cpfInexistente)).thenReturn(null);
        assertDoesNotThrow(() -> service.validarCpf(cpfInexistente));
    }

    @DisplayName("Se o token não  existir, lanca exception.")
    @Test
    public void validarToken(){
        String token = "token";
        assertThrows(RecursoNaoEncontradoException.class, () -> service.validarToken(null));
        assertDoesNotThrow(() -> service.validarToken(token));
    }



    @DisplayName("Se usuario já possui barbearia lançar exceptio")
    @Test
    public void validarSeUsuarioPossuiBarbearia(){
        String tokenMock = "token";
        Integer id = 2000;

        // Mock do usuário existente
        Barbeiro usuarioMock = new Barbeiro(id);

        // Mock dos barbeiros
        List<Barbeiro> barbeiros = new ArrayList<>();
        Barbeiro barbeiro = new Barbeiro(id);
        barbeiro.setBarbearia(new Barbearia());
        barbeiros.add(barbeiro);

        when(token.getUserIdByToken(tokenMock)).thenReturn(String.valueOf(id));
        when(usuarioRepository.findById(id)).thenReturn(of(usuarioMock));
        when(barbeiroRepository.findAll()).thenReturn(barbeiros);
        assertThrows(ConflitoException.class, () -> service.validarSeUsuarioPossuiBarbearia(tokenMock));
    }



    @DisplayName("Se o email já existir no banco, não deve criar o cliente")
    @Test
    public void seEmailExistirNaoCadastraCliente() {

        CadastroCliente nvCliente = new CadastroCliente("Nome", "test@example.com", "senhaSegura123", "123456789", "12345678", "Logradouro", 123, "Complemento", "Cidade", "Estado");

        Cliente usuarioExistente = new Cliente();
        usuarioExistente.setEmail("test@example.com");

        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(usuarioExistente);
        assertThrows(ConflitoException.class, () -> service.cadastrarCliente(nvCliente));

    }

    @DisplayName("Se não encontrar salvar o endereco no banco, não criar cliente")
    @Test
    public void seNaoSalvarEnderecoNaoCadastraCliente() {
        Endereco enderecoMock = new Endereco();
        enderecoMock.setId(1);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(null);

        CadastroCliente nvCliente = new CadastroCliente("Nome", "test@example.com", "senhaSegura123", "123456789", "12345678", "Logradouro", 123, "Complemento", "Cidade", "Estado");
        assertThrows(RecursoNaoEncontradoException.class, () -> service.cadastrarCliente(nvCliente));

    }

    @DisplayName("Criar cliente por DTO")
    @Test
    public void cadastrarCliente() {

        Endereco enderecoMock = new Endereco();
        enderecoMock.setId(1);
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoMock);

        when(token.getToken(any(Cliente.class))).thenReturn("mockToken");

        CadastroCliente nvCliente = new CadastroCliente("Nome", "test@example.com", "senhaSegura123", "123456789", "12345678", "Logradouro", 123, "Complemento", "Cidade", "Estado");

        Cliente clienteMock = new Cliente();
        clienteMock.setEndereco(new Endereco());

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteMock);

        String tokenMock = service.cadastrarCliente(nvCliente);

        assertNotNull(tokenMock);
        assertEquals("mockToken", tokenMock);

        verify(clienteRepository).save(any(Cliente.class));
        verify(token).getToken(any(Cliente.class));
    }


    @DisplayName("Se já existir cpf, não criar barbearia")
    @Test
    public void validarCpfBarbearia(){
        String cpf = "cpf";
        String token = "token";

        CadastroBarbearia nvBarbearia = new CadastroBarbearia();
        nvBarbearia.setCpf(cpf);

        Barbearia barbearia = new Barbearia();
        barbearia.setCpf(cpf);

        when(barbeariasRepository.findByCpf(cpf)).thenReturn(barbearia);
        assertThrows(ConflitoException.class, () -> service.cadastrarBarbeariaByDto(nvBarbearia, token));
    }

    @DisplayName("Se o usuário já tiver uma barbearia, não criar barbearia")
    @Test
    public void validarSeUsuarioPossuiCriacaoBarbearia(){
        String tokenMock = "token";
        CadastroBarbearia nvBarbearia = new CadastroBarbearia();
        Integer id = 2000;
        Barbeiro usuarioMock = new Barbeiro(id);

        List<Barbeiro> barbeiros = new ArrayList<>();
        Barbeiro barbeiro = new Barbeiro(id);
        barbeiro.setBarbearia(new Barbearia());
        barbeiros.add(barbeiro);

        when(token.getUserIdByToken(tokenMock)).thenReturn(String.valueOf(id));
        when(usuarioRepository.findById(id)).thenReturn(of(usuarioMock));
        when(barbeiroRepository.findAll()).thenReturn(barbeiros);
        assertThrows(ConflitoException.class, () -> service.cadastrarBarbeariaByDto(nvBarbearia, tokenMock));
    }


    @DisplayName("Cadastro de endereço pelo dto CadastroBarbea")
    @Test
    public void cadastroEndereco(){
        Integer id = 3434;

        CadastroBarbearia barbearia = mock(CadastroBarbearia.class);
        Endereco endereco = new Endereco();
        endereco.setId(id);

        when(barbearia.gerarEndereco()).thenReturn(endereco);
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        var resultado = service.cadastroEndereco(barbearia);
        assertEquals(id, resultado.getId());
    }


    @DisplayName("Se o endereco não estiver no banco, não cadastrar barbearia")
    @Test
    public void enderecoNaoEncontradoNaoCadastrarBarbearia(){
        Endereco endereco = new Endereco();
        CadastroBarbearia dto = mock(CadastroBarbearia.class);
        Barbearia barbearia = new Barbearia();

        when(dto.gerarBarbearia()).thenReturn(barbearia);
        when(enderecoRepository.existsById(endereco.getId())).thenReturn(false);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.cadastroBarbearia(dto, endereco));
    }

    @DisplayName("Cadastro de barbearia")
    @Test
    public void cadastroBarbearia(){
        Integer id = 3434;
        Integer idBarbearia = 2222;
        CadastroBarbearia dto = mock(CadastroBarbearia.class);

        Endereco endereco = new Endereco();
        endereco.setId(id);

        Barbearia barbearia = new Barbearia();
        barbearia.setId(idBarbearia);
        barbearia.setEndereco(endereco);

        when(dto.gerarBarbearia()).thenReturn(barbearia);
        when(enderecoRepository.getReferenceById(endereco.getId())).thenReturn(endereco);
        when(barbeariasRepository.save(barbearia)).thenReturn(barbearia);
        when(enderecoRepository.existsById(endereco.getId())).thenReturn(true);

        var resultado = service.cadastroBarbearia(dto, endereco);
        assertEquals(idBarbearia, resultado.getId());
        assertEquals(id, resultado.getEndereco().getId());
        assertDoesNotThrow(() -> service.cadastroBarbearia(dto, endereco));
    }


    @DisplayName("Atualizar cliente para barbeiro, setar a barbearia e como usuario adm")
    @Test
    @Transactional
    public void cadastrarBarbeariaByDto() {
        String tokenMock = "someToken";
        Integer userId = 1;
        Integer barbeariaId = 2929;

        Barbearia barbearia = new Barbearia();
        barbearia.setNomeNegocio("Barbearia Teste");
        barbearia.setId(barbeariaId);

        Cliente cliente = new Cliente();
        cliente.setId(userId);
        Endereco endereco = new Endereco();

        CadastroBarbearia nvBarbearia = mock(CadastroBarbearia.class);
        nvBarbearia.setCpf("12345678900");

        List<Barbeiro> barbeiros = new ArrayList<>();

        when(nvBarbearia.gerarBarbearia()).thenReturn(barbearia);
        when(token.getUserIdByToken(tokenMock)).thenReturn(String.valueOf(userId));
        when(barbeariasRepository.findByCpf(nvBarbearia.getCpf())).thenReturn(null);
        when(enderecoRepository.existsById(endereco.getId())).thenReturn(true);
        when(service.cadastroEndereco(nvBarbearia)).thenReturn(endereco);
        when(enderecoRepository.getReferenceById(endereco.getId())).thenReturn(endereco);
        when(usuarioRepository.findById(userId)).thenReturn(of(cliente));
        when(barbeiroRepository.findAll()).thenReturn(barbeiros);
        when(service.cadastroBarbearia(nvBarbearia, endereco)).thenReturn(barbearia);


        Barbearia result = service.cadastrarBarbeariaByDto(nvBarbearia, tokenMock);
        verify(clienteRepository, times(1)).atualizarClienteParaBarbeiro(userId, barbearia, true);
        assertNotNull(result);
        assertEquals(barbearia.getId(), result.getId());
        assertDoesNotThrow(() ->service.cadastrarBarbeariaByDto(nvBarbearia, tokenMock));
    }


    @DisplayName("Editar informações do usuário que é um cliente")
    @Test
    public void editarUsuarioCliente(){
        Integer id = 5678;
        String email = "email";
        String tokenMock = "tokenCliente";

        UsuarioConsulta dto = mock(UsuarioConsulta.class);
        dto.setEmail(email);

        Endereco endereco = new Endereco();

        Cliente cliente = new Cliente();
        cliente.setSenha("senha");
        cliente.setId(id);
        cliente.setEndereco(endereco);


        Cliente usuario = mapper.map(dto, Cliente.class);
        usuario.setSenha(cliente.getSenha());
        usuario.setId(cliente.getId());
        usuario.setEndereco(cliente.getEndereco());

        when(token.getUserIdByToken(tokenMock)).thenReturn(String.valueOf(id));
        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.getDtypeById(cliente.getId())).thenReturn("Cliente");
        when(clienteRepository.findById(id)).thenReturn(of(cliente));
        when(clienteRepository.save(usuario)).thenReturn(usuario);

        assertNotNull(service.editarUsuario(tokenMock, dto));
        var result = service.editarUsuario(tokenMock, dto);
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(cliente.getEmail(), result.getEmail());
        assertEquals(cliente.getEndereco(), usuario.getEndereco());
        assertEquals(cliente.getId(), usuario.getId());
        assertEquals(cliente.getSenha(), usuario.getSenha());
    }

    @DisplayName("Editar informações do usuário que é um barbeiro")
    @Test
    public void editarUsuarioBarbeiro(){
        Integer id = 5678;
        String email = "email";
        String tokenMock = "tokenBarbeiro";

        UsuarioConsulta dto = mock(UsuarioConsulta.class);
        dto.setEmail(email);

        Endereco endereco = new Endereco();
        Barbearia barbearia = new Barbearia();

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setId(id);
        barbeiro.setSenha("senha");
        barbeiro.setAdm(true);
        barbeiro.setBarbearia(barbearia);
        barbeiro.setEndereco(endereco);

        Barbeiro usuario = mapper.map(dto, Barbeiro.class);
        usuario.setId(barbeiro.getId());
        usuario.setSenha(barbeiro.getSenha());
        usuario.setAdm(barbeiro.isAdm());
        usuario.setBarbearia(barbeiro.getBarbearia());
        usuario.setEndereco(barbeiro.getEndereco());

        when(token.getUserIdByToken(tokenMock)).thenReturn(String.valueOf(id));
        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.getDtypeById(barbeiro.getId())).thenReturn("Barbeiro");
        when(barbeiroRepository.findById(id)).thenReturn(of(barbeiro));
        when(barbeiroRepository.save(usuario)).thenReturn(usuario);

        assertNotNull(service.editarUsuario(tokenMock, dto));
        var result = service.editarUsuario(tokenMock, dto);
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(barbeiro.getEmail(), result.getEmail());
        assertEquals(barbeiro.getEndereco(), usuario.getEndereco());
        assertEquals(barbeiro.getId(), usuario.getId());
        assertEquals(barbeiro.getSenha(), usuario.getSenha());
    }


    @DisplayName("Login válido, retorna token")
    @Test
    public void loginIsValid(){
        LoginUsuario mockUser = mock(LoginUsuario.class);
        String tokenMock = "token";
        Barbeiro barbeiro = new Barbeiro();

        when(usuarioRepository.findByEmailAndSenha(mockUser.getEmail(), mockUser.getSenha())).thenReturn(barbeiro);
        when(token.getToken(barbeiro)).thenReturn(tokenMock);

        var result = service.loginIsValid(mockUser);
        assertEquals(tokenMock, result);
    }

    @DisplayName("Login inválido, retorna exception")
    @Test
    public void loginInvalido(){
        LoginUsuario mockUser = mock(LoginUsuario.class);
        when(usuarioRepository.findByEmailAndSenha(mockUser.getEmail(), mockUser.getSenha())).thenReturn(null);
        assertThrows(RecursoNaoEncontradoException.class, () -> service.loginIsValid(mockUser));

    }

    @DisplayName("retornar o perfil, pelo token")
    @Test
    public void getPerfil(){
       Integer id = 1212;
       String tokenMock = "token";
       Barbeiro barbeiro = new Barbeiro(id);
       barbeiro.setEmail("email");
       PerfilUsuarioConsulta perfil = new PerfilUsuarioConsulta(barbeiro);

        when(token.getUserIdByToken(tokenMock)).thenReturn(String.valueOf(id));
        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.findById(id)).thenReturn(of(barbeiro));

        var result = service.getPerfil(tokenMock);
        assertEquals(perfil.getEmail(), result.getEmail());
    }

    @DisplayName("retornar o tipo, pelo token")
    @Test
    public void getUsuario(){
        Integer id = 1212;
        String tokenMock = "token";
        Barbeiro barbeiro = new Barbeiro(id);


        when(token.getUserIdByToken(tokenMock)).thenReturn(String.valueOf(id));
        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.findById(id)).thenReturn(of(barbeiro));

        DtypeConsulta tipoConsulta = mapper.map(barbeiro, DtypeConsulta.class);
        var result = service.getUsuario(tokenMock);
        assertEquals(tipoConsulta.getNome(), result.getNome());
        assertEquals(tipoConsulta.getDtype(), result.getDtype());
    }

    @Test
    public void testGetImage() throws IOException {
        String tk = "someToken";
        Integer userId = 123;
        String imageName = "image.png";

        // Create a sample image and convert it to bytes
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        when(usuarioRepository.existsById(userId)).thenReturn(true);
        when(token.getUserIdByToken(tk)).thenReturn(userId.toString());
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(new Usuario() {{
            setImgPerfil(imageName);
        }}));
        when(azureStorageService.getBlob(imageName)).thenReturn(imageBytes);

        ByteArrayResource result = service.getImage(tk);
        assertNotNull(result);

    }

    @Test
    public void testEditarImgPerfil_Success() throws IOException {
        String tk = "someToken";
        Integer userId = 123;
        String imageUrl = "http://example.com/image.png";
        MultipartFile file = mock(MultipartFile.class);

        Barbeiro usuario = new Barbeiro();
        usuario.setId(userId);
        usuario.setImgPerfil("oldImageUrl");

        when(usuarioRepository.existsById(userId)).thenReturn(true);
        when(token.getUserIdByToken(tk)).thenReturn(userId.toString());
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(azureStorageService.uploadImage(file)).thenReturn(imageUrl);


        ImgConsulta result = service.editarImgPerfil(tk, file);

        assertNotNull(result);
        assertEquals(imageUrl, result.getImagemPerfil());
        verify(usuarioRepository).save(usuario);
        assertEquals(imageUrl, usuario.getImgPerfil());
    }

    @Test
    public void testEditarImgPerfil_IOException() throws IOException {
        String tk = "someToken";
        Integer userId = 123;
        MultipartFile file = mock(MultipartFile.class);

        Barbeiro usuario = new Barbeiro();
        usuario.setId(userId);
        usuario.setImgPerfil("oldImageUrl");

        // Mocking dependencies
        when(usuarioRepository.existsById(userId)).thenReturn(true);
        when(token.getUserIdByToken(tk)).thenReturn(userId.toString());
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(azureStorageService.uploadImage(file)).thenThrow(new IOException());

        assertThrows(ErroServidorException.class,  () -> service.editarImgPerfil(tk, file));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }


}
