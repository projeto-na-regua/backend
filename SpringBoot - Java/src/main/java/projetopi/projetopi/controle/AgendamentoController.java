package projetopi.projetopi.controle;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import projetopi.projetopi.dominio.Agendamento;
import projetopi.projetopi.dto.request.ParametrosApi;
import projetopi.projetopi.dto.response.api.Previsao;
import projetopi.projetopi.repositorio.AgendaRepository;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin("*")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController{

    @Autowired
    private AgendaRepository repository;


    private static final Logger log = LoggerFactory.getLogger(AgendamentoController.class);
    @GetMapping("/previsao")
    @Operation(summary = "Buscar dados de temperatura", description = """
      # Busca os dados de um endereço a partir do CEP utilizando uma API externa
      ---
      Retorna os dados de endereço retornados da API.
      """)
    public ResponseEntity<Previsao> buscarEndereco() {

        RestClient client = RestClient.builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/forecast?lat=-9.8517&lon=100.6738&appid=ce698585477f529bd7a1914dd7cd7bb6")
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        String raw = client.get()
                .uri("")
                .retrieve()
                .body(String.class);

        log.info("Resposta da API: " + raw);

        Previsao previsao = client.get()
                .uri("")
                .retrieve()
                .body(Previsao.class);

        if (previsao == null) {
            return ResponseEntity.noContent().build();
        }

//        EnderecoDto resposta = new EnderecoDto();
//        resposta.setBairro(endereco.getBairro());
//        resposta.setCep(endereco.getCep());
//        resposta.setCidade(endereco.getCidade());
//        resposta.setEstado(endereco.getEstado());
//        resposta.setRua(endereco.getRua());

        return ResponseEntity.ok(previsao);
    }

    @PostMapping
    public ResponseEntity<Agendamento> adicionarAgendamento(@RequestBody Agendamento a){
        repository.save(a);
        return ResponseEntity.status(201).body(a);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> getAgendamento(){
        var lista = repository.findAll();
        return lista.isEmpty()
                ? status(204).build()
                : status(200).body(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizarAgendamento(@RequestBody Agendamento a,
                                                            @PathVariable Integer id){
        if (repository.existsById(id)) {
            a.setId(id);
            repository.save(a);
            return status(200).body(a);
        }
        return status(404).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Agendamento> deletarAgendamento(@PathVariable Integer id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return status(204).build();
        }
        return status(404).build();
    }

}
