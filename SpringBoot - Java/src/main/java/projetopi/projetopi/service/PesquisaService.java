package projetopi.projetopi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.dto.response.BarbeariaAvaliacao;
import projetopi.projetopi.dto.response.BarbeariaPesquisa;
import projetopi.projetopi.dto.response.Coordenada;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.ClienteRepository;
import projetopi.projetopi.repository.EnderecoRepository;
import projetopi.projetopi.repository.UsuarioRepository;
import projetopi.projetopi.util.Global;
import projetopi.projetopi.util.Token;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PesquisaService {


    @Autowired
    private Global global;
    @Autowired
    private BarbeariasRepository barbeariasRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    private Token tk;
    @Autowired
    private  ImageService imageService;


    public List<BarbeariaPesquisa> filtroBarberiasNome(String token, String nomeBarbearia) {
        global.validaCliente(token, "Cliente");
        List<Barbearia> barbearias = barbeariasRepository.findByNomeNegocioContaining(nomeBarbearia);
        return barbearias.stream().map(BarbeariaPesquisa::new).collect(Collectors.toList());
    }


    public Endereco editarEndereco(Endereco endereco, Integer id) {
        Endereco endereco1 = enderecoRepository.findById(id).get();
        endereco1.setId(id);
        return  enderecoRepository.save(endereco);
    }


    public List<BarbeariaPesquisa> getAllByLocalizacao(String token, String servico, LocalDate date, LocalTime time, Double raio) {

        Usuario cliente = usuarioRepository.findById(Integer.valueOf(tk.getUserIdByToken(token)))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", Integer.valueOf(tk.getUserIdByToken(token))));


        Endereco endereco = enderecoRepository.findById(cliente.getEndereco().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Endereço",cliente.getEndereco().getId()));

        String ponto = String.format("POINT(%f %f)", endereco.getLocalizacao().getY(), endereco.getLocalizacao().getX());


        String dia3Letras = date == null ? null : date.format(DateTimeFormatter.ofPattern("EEE", new Locale("pt")))
                .substring(0, 3).toUpperCase();



        List<Object[]> resultados = barbeariasRepository.findBarbeariasProximasByTipoServicoEDisponibilidadeComMedia(
                ponto,
                raio == null ? 2000 : raio,
                servico,
                dia3Letras,
                time);


        return dtoList(resultados);
    }

    public List<BarbeariaPesquisa> getAllByLocalizacaoSemCadastro(String servico, LocalDate date, LocalTime time, String cep, Double raio) {

        List<Barbearia> barbearias = barbeariasRepository.findBarbeariasByTipoServico(servico);
        List<BarbeariaPesquisa> barbeariasProximas = new ArrayList<>();



        Coordenada coordenada  = enderecoService.gerarCoordenadas(cep);

        double latitude = Double.parseDouble(coordenada.getLat());
        double longitude = Double.parseDouble(coordenada.getLng());

        if (latitude == 0 || longitude == 0) {
            throw new IllegalArgumentException("Latitude ou Longitude inválidos.");
        }

        String ponto = String.format("POINT(%f %f)", longitude, latitude);


        String dia3Letras = date == null ? null :date.format(DateTimeFormatter.ofPattern("EEE", new Locale("pt")))
                .substring(0, 3).toUpperCase();


        List<Object[]> resultados = barbeariasRepository.findBarbeariasProximasByTipoServicoEDisponibilidadeComMedia(
                ponto,
                raio == null ? 2000 : raio,
                servico,
                dia3Letras,
                time);

        return dtoList(resultados);
    }



    public List<BarbeariaAvaliacao> getTop3Melhores() {
        List<BarbeariaAvaliacao> lista = barbeariasRepository.findTopBarbearias();

        if (lista.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        for (BarbeariaAvaliacao ba : lista){
            if (ba.getImgPerfilBarbearia() != null && !ba.getImgPerfilBarbearia().isEmpty()) {
                ba.setImgPerfilBarbearia(imageService.getImgURL(ba.getImgPerfilBarbearia(), "barbearia"));
            }
        }

        return lista;
    }


    public List<BarbeariaPesquisa> dtoList(List<Object[]> resultados){
        List<BarbeariaPesquisa> barbeariasPesquisadas = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Integer idBarbearia = (Integer) resultado[0];
            String nomeNegocio = (String) resultado[1];
            String emailNegocio = (String) resultado[2];
            String celularNegocio = (String) resultado[3];
            String cnpj = (String) resultado[4];
            String cpf = (String) resultado[5];
            String descricao = (String) resultado[6];
            Integer barbeariaFkEndereco = (Integer) resultado[7];
            String imgPerfil = (String) resultado[8];
            String imgBanner = (String) resultado[9];
            BigDecimal mediaAvaliacoes = (BigDecimal) resultado[10];

            // Criar um novo objeto Barbearia com os dados retornados
            Barbearia barbearia = new Barbearia();
            barbearia.setId(idBarbearia);
            barbearia.setNomeNegocio(nomeNegocio);
            barbearia.setEmailNegocio(emailNegocio);
            barbearia.setCelularNegocio(celularNegocio);
            barbearia.setCnpj(cnpj);
            barbearia.setCpf(cpf);
            barbearia.setDescricao(descricao);
            barbearia.setEndereco(enderecoRepository.findById(barbeariaFkEndereco).get());
            barbearia.setImgPerfil(imgPerfil);
            barbearia.setImgBanner(imgBanner);
            String linkImg = null;

            if (barbearia.getImgPerfil() != null && !barbearia.getImgPerfil().isEmpty()) {
                linkImg = imageService.getImgURL(barbearia.getImgPerfil(), "barbearia");
            }
            barbeariasPesquisadas.add(new BarbeariaPesquisa(barbearia, mediaAvaliacoes, linkImg));
        }

        return barbeariasPesquisadas;
    }

    public List<BarbeariaPesquisa> dtoLisBarbearia(List<Barbearia> resultados){
        List<BarbeariaPesquisa> barbeariasPesquisadas = new ArrayList<>();

        for (Barbearia barbearia : resultados) {
            BarbeariaPesquisa barbeariaPesquisa = new BarbeariaPesquisa(barbearia);

            if (barbearia.getImgPerfil() != null && !barbearia.getImgPerfil().isEmpty()) {
                barbeariaPesquisa.setImgPerfil(imageService.getImgURL(barbearia.getImgPerfil(), "barbearia"));
            }

            barbeariasPesquisadas.add(barbeariaPesquisa);
        }

        return barbeariasPesquisadas;
    }




}
