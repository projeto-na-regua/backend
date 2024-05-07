package projetopi.projetopi.controle;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projetopi.projetopi.dominio.api.Precipitacao;
import projetopi.projetopi.dominio.api.Temperatura;
import projetopi.projetopi.dto.mappers.PrevisaoMapper;
import projetopi.projetopi.dto.response.PrevisaoApi;
import projetopi.projetopi.service.FiltrosService;
import projetopi.projetopi.util.ListaObj;

@RestController
@RequestMapping("/filtros")
public class FiltroController {

    @Autowired
    private FiltrosService service;

    @GetMapping("/previsao")
    @Operation(summary = "Previsão de tempo próximos 7 dias", description = """
            # Listar o horario e data, precipitação e temperatura utilizando uma API externa
            ---
            Retorna um vetor com a precipitação e temperatura das próximas 168 horas.
            """)
    @ApiResponse(responseCode = "200", description = "Previsão")

    public ResponseEntity<ListaObj<PrevisaoApi>> getTemperatura() {

        Temperatura t = service.getTemperatura();
        Precipitacao p = service.getPrecipitacao();

        if (service.getTemperatura() == null) {
            return ResponseEntity.noContent().build();
        }

        if (service.getPrecipitacao() == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new PrevisaoMapper().toDto(t, p));

    }


    @GetMapping("/horario/{tipoFiltro}")
    @Operation(summary = "Previsão de tempo próximos 7 dias", description = """
            # Listar o horario e data, precipitação e temperatura utilizando uma API externa
            ---
            Retorna um vetor com a precipitação e temperatura das próximas 168 horas.
            """)
    @ApiResponse(responseCode = "200", description = "Previsão")
    public ResponseEntity<ListaObj<PrevisaoApi>> getTemperatura(@PathVariable String tipoFiltro) {

        Temperatura t = service.getTemperatura();
        Precipitacao p = service.getPrecipitacao();

        if (service.getTemperatura() == null) {
            return ResponseEntity.noContent().build();
        }

        if (service.getPrecipitacao() == null) {
            return ResponseEntity.noContent().build();
        }


        ListaObj<PrevisaoApi> v = new PrevisaoMapper().toDto(t, p);

        if(tipoFiltro.equals("temperatura-alta")){
            service.ordenarPorTemperaturasAltas(v, 0, v.getTamanho() -1, true, "t");
        }else if (tipoFiltro.equals("temperatura-baixa")){
            service.ordenarPorTemperaturasAltas(v, 0, v.getTamanho() -1, false, "t");
        }else if(tipoFiltro.equals("precipitacao")){
            service.ordenarPorTemperaturasAltas(v,0, v.getTamanho() -1, false, "p");
        }

        return ResponseEntity.ok(v);

    }

//    @GetMapping("/horario-temperaturas-baixas")
//    @Operation(summary = "Previsão de tempo próximos 7 dias", description = """
//            # Listar o horario e data, precipitação e temperatura utilizando uma API externa
//            ---
//            Retorna um vetor com a precipitação e temperatura das próximas 168 horas.
//            """)
//    @ApiResponse(responseCode = "200", description = "Previsão")
//    public ResponseEntity<PrevisaoApi[]> getTemperaturaBaixas() {
//
//        Temperatura t = service.getTemperatura();
//        Precipitacao p = service.getPrecipitacao();
//
//        if (service.getTemperatura() == null) {
//            return ResponseEntity.noContent().build();
//        }
//
//        if (service.getPrecipitacao() == null) {
//            return ResponseEntity.noContent().build();
//        }
//
//        PrevisaoApi[] v = new PrevisaoMapper().toDto(t, p);
//
//        service.ordenarPorTemperaturasBaixas(v, 0, v.length -1);
//        return ResponseEntity.ok(v);
//    }
//
//    @GetMapping("/horario-precipitacao-baixa")
//    @Operation(summary = "Previsão da precipitação dos próximos 7 dias", description = """
//            # Listar o horario e data, precipitação e temperatura utilizando uma API externa
//            ---
//            Retorna um vetor com a precipitação e temperatura das próximas 168 horas.
//            """)
//    @ApiResponse(responseCode = "200", description = "Previsão")
//    public ResponseEntity<PrevisaoApi[]> getPrecipitacaoBaixa() {
//
//        Temperatura t = service.getTemperatura();
//        Precipitacao p = service.getPrecipitacao();
//
//        if (service.getTemperatura() == null) {
//            return ResponseEntity.noContent().build();
//        }
//
//        if (service.getPrecipitacao() == null) {
//            return ResponseEntity.noContent().build();
//        }
//
//        PrevisaoApi[] v = new PrevisaoMapper().toDto(t, p);
//
//        service.ordenarPorPrecipitacao(v, 0, v.length -1);
//        return ResponseEntity.ok(v);
//    }


}
