package projetopi.projetopi.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.dto.request.BarbeiroCriacao;

import projetopi.projetopi.dto.response.BarbeiroConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.entity.BarbeiroServico;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.AcessoNegadoException;
import projetopi.projetopi.exception.ConflitoException;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.relatorios.RelatorioBarbeiro;
import projetopi.projetopi.repository.*;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.status;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeiroServicoRepository barbeiroServicoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ImageService imageService;


    @Autowired
    public Token tk;

    @Autowired
    public ModelMapper mapper;

    @Autowired
    private Global global;

    @Autowired
    private UsuarioMapper usuarioService;

    public boolean isCliente(String token){
        return usuarioRepository.findById(Integer.valueOf(tk.getUserIdByToken(token))).get().getDtype().equals("Cliente");
    }


    public List<BarbeiroConsulta> getFuncionarios(String token){
        validarPermissioes(token);

        List<Barbeiro> barbeiros = barbeiroRepository.findByBarbeariaIdAndUsuarioIdNot(getBarbeariaByToken(token).getId());
        if (barbeiros.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return usuarioService.toDto(barbeiros);
    }

    public List<BarbeiroConsulta> getFuncionariosCliente(String token, Integer idBarbearia){
        global.validaCliente(token, "Cliente");

        List<Barbeiro> barbeiros = barbeiroRepository.findByBarbeariaId(idBarbearia);
        if (barbeiros.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return usuarioService.toDto(barbeiros);
    }

    public List<String> getImagePerfilCliente(String token, Integer idBarbearia) {
        global.validaCliente(token, "Cliente");
        try {
            List<String> imageUrlList = new ArrayList<>();
            for (Usuario barbeiro : usuarioRepository.findAllByBarbeariaId(idBarbearia)) {
                if (barbeiro.getImgPerfil() != null && !barbeiro.getImgPerfil().isEmpty()) {
                    String imageUrl = imageService.getImgURL(barbeiro.getImgPerfil(), "usuario");
                    imageUrlList.add(imageUrl);
                }

            }
            System.out.println(imageUrlList);
            return imageUrlList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<BarbeiroConsulta> getFuncionariosByServico(String token, Integer idServico){
        validarPermissioes(token);

        if (!servicoRepository.existsById(idServico)) {
            throw new RecursoNaoEncontradoException("Servico", idServico);
        }

        List<BarbeiroServico> barbeirosServico = barbeiroServicoRepository.findByServicoIdAndBarbeariaId(idServico, getBarbeariaByToken(token).getId());
        List<Barbeiro> barbeiros = new ArrayList<>();

        for (BarbeiroServico bs: barbeirosServico){
            barbeiros.add(barbeiroRepository.findById(bs.getBarbeiro().getId()).get());
        }


        if (barbeiros.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return usuarioService.toDto(barbeiros);
    }

    public List<BarbeiroConsulta> getFuncionariosByServicoForCliente(String token, Integer idServico){

        global.validaCliente(token, "Cliente");

        if (!servicoRepository.existsById(idServico)) {
            throw new RecursoNaoEncontradoException("Servico", idServico);
        }

        List<BarbeiroServico> barbeirosServico = barbeiroServicoRepository.findByServicoIdAndBarbeariaId(idServico, servicoRepository.findById(idServico).get().getBarbearia().getId());
        List<Barbeiro> barbeiros = new ArrayList<>();

        for (BarbeiroServico bs: barbeirosServico){
            barbeiros.add(barbeiroRepository.findById(bs.getBarbeiro().getId()).get());
        }

        if (barbeiros.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        return usuarioService.toDto(barbeiros);
    }

    public BarbeiroConsulta getFuncionarioEspecifico(String email, String token){
        validarPermissioes(token);
        validarBarbeiro(email);
        Barbeiro barbeiro = barbeiroRepository.findByEmail(email);
        return new BarbeiroConsulta(barbeiro, barbeiro.getImgPerfil() == null ? null : imageService.getImgURL(barbeiro.getImgPerfil(), "usuario"));
    }


    public BarbeiroConsulta criarBarbeiro(String token, BarbeiroCriacao nvBarbeiro){
        validarPermissioes(token);
        validarBarbeiroConflito(nvBarbeiro.getEmail());
        Barbeiro b = mapper.map(nvBarbeiro, Barbeiro.class);
        b.setBarbearia(getBarbeariaByToken(token));
        Barbeiro barbeiro = barbeiroRepository.save(b);
        return new BarbeiroConsulta(barbeiro, barbeiro.getImgPerfil() == null ? null : imageService.getImgURL(barbeiro.getImgPerfil(), "usuario"));
    }


    public void adicionarBarberiro(String token, String email){
        validarPermissioes(token);
        validarUsuarioComumExiste(email);
        clienteRepository.atualizarClienteParaBarbeiro(clienteRepository.findByEmail(email).getId(), getBarbeariaByToken(token), false);

    }

    public UsuarioConsulta buscarEmailBarbeiro(String email, String token){
        validarPermissioes(token);
        validarUsuarioComumExiste(email);
        return mapper.map(clienteRepository.findByEmail(email), UsuarioConsulta.class);
    }



    public void deleteBarbeiro(String email, String token){
        validarPermissioes(token);
        validarBarbeiro(email);
        barbeiroRepository.atualizarBarbeiroParaCliente(barbeiroRepository.findByEmail(email).getId());
    }

    public Barbeiro getBarbeiroByToken(String token){
        Integer id  = Integer.valueOf(tk.getUserIdByToken(token));
        return barbeiroRepository.findById(id).get();
    }

    public Barbearia getBarbeariaByToken(String token){
        Barbeiro b = getBarbeiroByToken(token);
        return b.getBarbearia();
    }

    public byte[] gerarRelatorio(String token){
        validarPermissioes(token);
        List<Barbeiro> barbeiros = mapper.map(getFuncionarios(token), new TypeToken<List<Barbeiro>>(){}.getType());
        if (barbeiros.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204));
        }

        // Gerar o arquivo CSV
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        RelatorioBarbeiro.gravarRelatorioFinanceiro(barbeiros, outputStream);

        return outputStream.toByteArray();
    }

    public HttpHeaders configurarHeadears(){
        String nomeArquivo = "relatório_barbeiros.csv";

        // Configurar os headers para o download do arquivo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(nomeArquivo, nomeArquivo);

        return headers;
    }

   void validarPermissioes(String token){
        if(isCliente(token) ||
           getBarbeariaByToken(token) == null ||
           !getBarbeiroByToken(token).isAdm()){
            throw new AcessoNegadoException("Usuario");

        }

    }

    void validarBarbeiro(String email){
        if(barbeiroRepository.findByEmail(email) == null){
            throw new RecursoNaoEncontradoException("Barbeiro", email);
        }
    }

    void validarBarbeiroConflito(String email){
        if(barbeiroRepository.findByEmail(email) != null){
            throw new ConflitoException("Barbeiro", email);
        }
    }

   void validarUsuarioComumExiste(String email){
        if (clienteRepository.findByEmail(email) == null){
            throw new RecursoNaoEncontradoException("Usuário", email);
        }
    }


    public List<BarbeiroConsulta> findByName(String nome, String token) {
        validarPermissioes(token);

        List<Barbeiro> funcionarios = barbeiroRepository.findByNomeContaining(nome);

        if (funcionarios.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        List<BarbeiroConsulta> dtos = new ArrayList<>();
        for (Barbeiro b : funcionarios){
            dtos.add(new BarbeiroConsulta(b));
        }

        return dtos;
    }

    public String editarNome(Integer id, String nome) {

        Barbeiro barbeiro = barbeiroRepository.findById(id).get();
        barbeiro.setNome(nome);
        barbeiro.setId(barbeiro.getId());
        barbeiroRepository.save(barbeiro);

        return nome;
    }
}
