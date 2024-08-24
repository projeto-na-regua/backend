package projetopi.projetopi.service;


import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import projetopi.projetopi.controller.UsuarioController;
import projetopi.projetopi.dto.mappers.UsuarioMapper;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.response.Coordenada;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.EnderecoRepository;
import projetopi.projetopi.util.Global;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private Global global;


    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private GeometryFactory geometryFactory = new GeometryFactory();

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



    public void setCoordenadas(Endereco endereco) {
        Coordenada coordenada = gerarCoordenadas(endereco.getCep());

        double latitude = Double.parseDouble(coordenada.getLat());
        double longitude = Double.parseDouble(coordenada.getLng());

        // Criação do ponto com a ordem correta (longitude, latitude)
        Point ponto = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        endereco.setLocalizacao(ponto);
    }

    public Endereco cadastroEndereco(CadastroBarbearia nvBarbearia) {
        Endereco endereco = nvBarbearia.gerarEndereco();
        setCoordenadas(endereco);
        return repository.save(endereco);
    }

    public Endereco cadastroEndereco(CadastroCliente nvCliente) {
        Endereco endereco = UsuarioMapper.toDtoEndereco(nvCliente);
        setCoordenadas(endereco);
        return repository.save(endereco);
    }

    public Endereco updateEndereco(Endereco endereco, Integer id){
        if (!repository.existsById(id)){
            throw  new RecursoNaoEncontradoException("Endereço", id);
        }
        setCoordenadas(endereco);
        return repository.save(endereco);
    }
}
