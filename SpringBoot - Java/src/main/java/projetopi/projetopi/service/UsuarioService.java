package projetopi.projetopi.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.dto.response.*;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Dia;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

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

    @Autowired
    private ImageService imageService;
    @Autowired
    private EnderecoService enderecoService;



    public UsuarioService(BarbeiroRepository barbeiroRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, BarbeariasRepository barbeariasRepository, DiaSemanaRepository diaSemanaRepository, UsuarioRepository usuarioRepository, Token token, ModelMapper mapper, Global global) {
        this.barbeiroRepository = barbeiroRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.barbeariasRepository = barbeariasRepository;
        this.diaSemanaRepository = diaSemanaRepository;
        this.usuarioRepository = usuarioRepository;
        this.token = token;
        this.mapper = mapper;
        this.global = global;
    }


    public String cadastrarCliente(CadastroCliente nvCliente, MultipartFile file){
        global.validarEmail(nvCliente.getEmail());
        global.validarUsername(nvCliente.getUsername());

        Endereco endereco = enderecoService.cadastroEndereco(nvCliente);
        global.validarEnderecoCadastrado(endereco);

        Integer idEndereco = endereco.getId();
        Cliente cliente = UsuarioMapper.toDto(nvCliente);

        if (file != null && !file.isEmpty()) {
            String fileName = imageService.upload(file, "usuario");
            cliente.setImgPerfil(fileName);
        }

        cliente.getEndereco().setId(idEndereco);

        clienteRepository.save(cliente);
        return token.getToken(cliente);
    }


    public Barbearia cadastroBarbearia(CadastroBarbearia nvBarbearia, Endereco endereco,  MultipartFile imgBanner, MultipartFile imgPerfil){
        Barbearia barbearia = nvBarbearia.gerarBarbearia();

        if (imgBanner != null && !imgBanner.isEmpty()) {
            String fileName = imageService.upload(imgBanner, "barbearia");
            barbearia.setImgBanner(fileName);
        }

        if (imgPerfil != null && !imgPerfil.isEmpty()) {
            String fileName = imageService.upload(imgPerfil, "barbearia");
            barbearia.setImgPerfil(fileName);
        }

        Endereco newEndereco = enderecoRepository
                .findById(endereco.getId()).orElseThrow(() -> new RecursoNaoEncontradoException("Endereco", endereco));


        barbearia.setEndereco(newEndereco);
        return barbeariasRepository.save(barbearia);
    }


    public Barbearia cadastrarBarbeariaByDto(CadastroBarbearia nvBarbearia, String tk, MultipartFile imgBanner, MultipartFile imgPerfil){
        global.validarCpf(nvBarbearia.getCpf());
        global.validarSeUsuarioPossuiBarbearia(tk);

        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        Endereco endereco = enderecoService.cadastroEndereco(nvBarbearia);



        Barbearia barbearia = cadastroBarbearia(nvBarbearia, endereco, imgBanner, imgPerfil);
        DiaSemana[] diaSemanas = definirDiasDaSemanda();

        for (DiaSemana diaSemana : diaSemanas){
            diaSemana.setBarbearia(barbearia);
            diaSemanaRepository.save(diaSemana);
        }

        clienteRepository.atualizarClienteParaBarbeiro(id, barbearia, true);
        return barbearia;
    }


    public String getImagePerfil(String tk) {
        Usuario usuario= usuarioRepository.findById(Integer.valueOf(token.getUserIdByToken(tk)))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", Integer.valueOf(token.getUserIdByToken(tk))));

        String imageName = usuario.getImgPerfil();

        return imageService.getImgURL(imageName, "usuario");
    }


    public UsuarioConsulta editarUsuario(String tk, UsuarioConsulta nvUsuario){
        Integer id = Integer.valueOf(token.getUserIdByToken(tk));
        global.validarUsuarioExiste(tk);
        global.validarUpdate(nvUsuario, usuarioRepository.findById(id).get());

        if(global.isCliente(tk)){
            Cliente clienteSalvo = clienteRepository.findById(id).get();
            Cliente usuario = mapper.map(nvUsuario, Cliente.class);

            usuario.setId(id);
            usuario.setSenha(clienteSalvo.getSenha());
            usuario.setEndereco(clienteSalvo.getEndereco());
            usuario.setImgPerfil(clienteSalvo.getImgPerfil());
            clienteRepository.save(usuario);
            return new UsuarioConsulta(usuario);

        }else{

            Barbeiro barbeiroSalvo = barbeiroRepository.findById(id).get();
            Barbeiro usuario = mapper.map(nvUsuario, Barbeiro.class);

            usuario.setId(barbeiroSalvo.getId());
            usuario.setSenha(barbeiroSalvo.getSenha());
            usuario.setAdm(barbeiroSalvo.isAdm());
            usuario.setBarbearia(barbeiroSalvo.getBarbearia());
            usuario.setEndereco(barbeiroSalvo.getEndereco());
            usuario.setImgPerfil(barbeiroSalvo.getImgPerfil());
            barbeiroRepository.save(usuario);
            return new UsuarioConsulta(usuario);
        }
    }



    public String loginIsValid(LoginUsuario user){
        Usuario u = usuarioRepository.findByEmailAndSenha(user.getEmail(), user.getSenha());

        if (u == null){
            throw new RecursoNaoEncontradoException("Usuário", user);
        }
        String tk = token.getToken(u);
        global.validarToken(tk);
        return tk;
    }


    public PerfilUsuarioConsulta getPerfil(String t){
        global.validarUsuarioExiste(t);
        PerfilUsuarioConsulta dto  = new PerfilUsuarioConsulta(global.getUsuarioByToken(t));
        if(dto.getImgPerfil() != null && !(dto.getImgPerfil().isEmpty())){
            dto.setImgPerfil(imageService.getImgURL(dto.getImgPerfil(), "usuario"));
        }
        return dto;

    }

    public DtypeConsulta getUsuario(String t){
        global.validarUsuarioExiste(t);
        return mapper.map(global.getUsuarioByToken(t), DtypeConsulta.class);
    }


    public ImgConsulta editarImgPerfil(String tk, MultipartFile file){
        global.validarUsuarioExiste(tk);

        String imageUrl = imageService.upload(file, "usuario");
        Usuario usuario = global.getUsuarioByToken(tk);
        usuario.setImgPerfil(imageUrl);
        usuarioRepository.save(usuario);
        return new ImgConsulta(usuario.getImgPerfil());

    }


    public DiaSemana[] definirDiasDaSemanda(){
        DiaSemana seg = new DiaSemana(Dia.SEG);
        DiaSemana ter = new DiaSemana(Dia.TER);
        DiaSemana qua = new DiaSemana(Dia.QUA);
        DiaSemana qui = new DiaSemana(Dia.QUI);
        DiaSemana sex = new DiaSemana(Dia.SEX);
        DiaSemana sab = new DiaSemana(Dia.SAB);
        DiaSemana dom = new DiaSemana(Dia.DOM);


        DiaSemana[] diaSemanas = {seg, ter, qua, qui, sex, sab, dom};
        return diaSemanas;
    }


}
