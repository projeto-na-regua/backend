package projetopi.projetopi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projetopi.projetopi.entity.api.Precipitacao;
import projetopi.projetopi.entity.api.Temperatura;
import projetopi.projetopi.dto.mappers.PrevisaoMapper;
import projetopi.projetopi.dto.response.PrevisaoApi;
import projetopi.projetopi.service.FiltrosService;
import projetopi.projetopi.util.ListaObj;

import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.*;

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
            return noContent().build();
        }

        if (service.getPrecipitacao() == null) {
            return noContent().build();
        }
        return ok(new PrevisaoMapper().toDto(t, p));

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
            return noContent().build();
        }

        if (service.getPrecipitacao() == null) {
            return noContent().build();
        }


        ListaObj<PrevisaoApi> v = new PrevisaoMapper().toDto(t, p);

        if(tipoFiltro.equals("temperatura-alta")){
            service.ordenarPrevisao(v, 0, v.getTamanho() -1, true, "t");
        }else if (tipoFiltro.equals("temperatura-baixa")){
            service.ordenarPrevisao(v, 0, v.getTamanho() -1, false, "t");
        }else if(tipoFiltro.equals("precipitacao")){
            service.ordenarPrevisao(v,0, v.getTamanho() -1, false, "p");
        }

        return ok(v);

    }

    @GetMapping("/pesquisa/{dataHora}")
    public ResponseEntity<PrevisaoApi> getHora(@PathVariable LocalDateTime dataHora){


        Temperatura t = service.getTemperatura();
        Precipitacao p = service.getPrecipitacao();

        if (service.getTemperatura() == null) {
            return noContent().build();
        }

        if (service.getPrecipitacao() == null) {
            return noContent().build();
        }

        ListaObj<PrevisaoApi> lista = PrevisaoMapper.toDto(t, p);
        int resultado = service.pesquisaBinaria(lista, dataHora);

        return resultado != -1 ? status(200).body(lista.getElemento(resultado)) : status(404).build();
    }


}
