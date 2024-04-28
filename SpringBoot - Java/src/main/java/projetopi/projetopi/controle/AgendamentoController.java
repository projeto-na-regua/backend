package projetopi.projetopi.controle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import projetopi.projetopi.dominio.Agendamento;
import projetopi.projetopi.dto.response.api.Dado;
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
    @Operation(summary = "Listar bancos", description = """
            # Listar todos os bancos utilizando uma API externa
            ---
            Retorna uma lista com todos os bancos disponíveis na API.
            """)
    @ApiResponse(responseCode = "200", description = "Lista de bancos")


    public ResponseEntity<Previsao> getpRevisao(@RequestParam(name = "lat") Double lat, @RequestParam(name = "lon") Double lon) {
        RestClient client = RestClient.builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        String raw = client.get()
                .uri("forecast?" + lat + lon + "&appid=ce698585477f529bd7a1914dd7cd7bb6")
                .retrieve()
                .body(String.class);

        log.info("Resposta da API: " + raw);

        Previsao previsao = client.get()
                .uri("weather" + lat + lon + "&appid=ce698585477f529bd7a1914dd7cd7bb6")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (previsao == null) {
            return ResponseEntity.noContent().build();
        }


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
