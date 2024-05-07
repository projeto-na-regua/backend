package projetopi.projetopi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import projetopi.projetopi.controle.FiltroController;
import projetopi.projetopi.dominio.api.Precipitacao;
import projetopi.projetopi.dominio.api.Temperatura;
import projetopi.projetopi.dto.response.PrevisaoApi;
import projetopi.projetopi.util.ListaObj;

@Component
@PropertySource("classpath:application.properties")
@Service
public class FiltrosService {


    @Autowired
    private Environment env;

    private String token;

    // Constructor
    public FiltrosService() {
        // No construtor, você não terá acesso ao Environment injetado ainda,
        // portanto, você não pode inicializar 'token' aqui
    }


    public String getToken() {
        if (token == null) {
            token = env.getProperty("TOKEN_API");
        }
        return token;
    }

    private static final Logger log = LoggerFactory.getLogger(FiltroController.class);

    private String uri(boolean isTemperature){

        return """
                forecast/%s/locale/3477/hours/168?token=%s"""
                .formatted(isTemperature ? "temperature" : "precipitation", env.getProperty("TOKEN_API"));

    }

    private RestClient client(boolean isTemperature){

        RestClient client = RestClient.builder()
                .baseUrl("https://apiadvisor.climatempo.com.br/api/v2/")
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        String raw = client.get()
                .uri(uri(isTemperature))
                .retrieve()
                .body(String.class);

        log.info("Resposta da API: " + raw);
        return client;

    }

    public Temperatura getTemperatura() {

        Temperatura temperatura = client(true).get()
                .uri(uri(true))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });


        return temperatura;

    }


    public Precipitacao getPrecipitacao() {

        Precipitacao precipitacao = client(false).get()
                .uri(uri(false))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });


        return precipitacao;
    }

    public void ordenarPorTemperaturasBaixas(ListaObj<PrevisaoApi> v, int indInicio, int indFim){

        if (v == null || v.getTamanho() == 0 || indInicio >= indFim) {
            return;
        }
        int i = indInicio;
        int j = indFim;
        double pivo = v.getElemento((indInicio + indFim) / 2).getTemperatura();

        while (i <= j) {
            while (v.getElemento(i).getTemperatura() < pivo) {
                i++;
            }

            while (v.getElemento(j).getTemperatura() > pivo) {
                j--;
            }

            if (i <= j) {
                PrevisaoApi aux = v.getElemento(i);
                v.insereNaPosicao(v.getElemento(j), i);
                v.insereNaPosicao(aux, j);
                i++;
                j--;
            }
        }

        if (indInicio < j) {
            ordenarPorTemperaturasBaixas(v, indInicio, j);
        }

        if (i < indFim) {
            ordenarPorTemperaturasBaixas(v, i, indFim);
        }
    }

    public void ordenarPorTemperaturasAltas(ListaObj<PrevisaoApi> v, int indInicio, int indFim, boolean desc, String tipo) {
        if (v == null || v.getTamanho() == 0 || indInicio >= indFim) {
            return;
        }

        int i = indInicio;
        int j = indFim;

        double pivo = tipo == "t" ? v.getElemento((indInicio + indFim) / 2).getTemperatura() : v.getElemento((indInicio + indFim) / 2).getPrecipitacao();
        double elementoInicio = tipo == "t" ? v.getElemento(i).getTemperatura() : v.getElemento(i).getPrecipitacao();
        double elementoFim = tipo == "t" ? v.getElemento(j).getTemperatura() : v.getElemento(j).getPrecipitacao();

        while (i <= j) {
            while (desc ? elementoInicio > pivo : elementoInicio < pivo) {
                i++;
            }

            while (desc ? elementoFim < pivo : elementoFim > pivo) {
                j--;
            }

            if (i <= j) {
                PrevisaoApi aux = v.getElemento(i);
                v.insereNaPosicao(v.getElemento(j), i);
                v.insereNaPosicao(aux, j);

                i++;
                j--;
            }
        }

        if (indInicio < j) {
            ordenarPorTemperaturasAltas(v, indInicio, j, desc, tipo);
        }

        if (i < indFim) {
            ordenarPorTemperaturasAltas(v, i, indFim, desc, tipo);
        }
    }


    public void ordenarPorPrecipitacao(PrevisaoApi[] v, int indInicio, int indFim){

        if (v == null || v.length == 0 || indInicio >= indFim) {
            return; // Não há elementos para ordenar ou o intervalo é inválido
        }

        int i = indInicio;
        int j = indFim;
        double pivo = v[(indInicio + indFim) / 2].getPrecipitacao();

        while (i <= j) {
            while (v[i].getPrecipitacao() > pivo) {
                i++;
            }

            while (v[j].getPrecipitacao() < pivo) {
                j--;
            }

            if (i <= j) {
                PrevisaoApi aux = v[i];
                v[i] = v[j];
                v[j] = aux;

                i++;
                j--;
            }
        }

        if (indInicio > j) {
            ordenarPorPrecipitacao(v, indInicio, j);
        }

        if (i > indFim) {
            ordenarPorPrecipitacao(v, i, indFim);
        }
    }
}
