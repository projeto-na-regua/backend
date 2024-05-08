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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@PropertySource("classpath:application.properties")
@Service
public class FiltrosService {


    @Autowired
    private Environment env;

    private String token;

    // Constructor
    public FiltrosService() {}


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


    public void ordenarPrevisao(ListaObj<PrevisaoApi> v, int indInicio, int indFim, boolean desc, String tipo) {
        if (v == null || v.getTamanho() == 0 || indInicio >= indFim) {
            return;
        }

        int i = indInicio;
        int j = indFim;

        while (i <= j) {
            double pivo = tipo.equals("t") ? v.getElemento((indInicio + indFim) / 2).getTemperatura() : v.getElemento((indInicio + indFim) / 2).getPrecipitacao();
            double elementoInicio = tipo.equals("t") ? v.getElemento(i).getTemperatura() : v.getElemento(i).getPrecipitacao();
            double elementoFim = tipo.equals("t") ? v.getElemento(j).getTemperatura() : v.getElemento(j).getPrecipitacao();

            while (desc ? elementoInicio > pivo : elementoInicio < pivo) {
                i++;
                if (i >= v.getTamanho()) {
                    break; // Saída do loop se o índice ultrapassar o tamanho da lista
                }
                elementoInicio = tipo.equals("t") ? v.getElemento(i).getTemperatura() : v.getElemento(i).getPrecipitacao();
            }

            while (desc ? elementoFim < pivo : elementoFim > pivo) {
                j--;
                if (j < 0) {
                    break; // Saída do loop se o índice ficar negativo
                }
                elementoFim = tipo.equals("t") ? v.getElemento(j).getTemperatura() : v.getElemento(j).getPrecipitacao();
            }

            if (i <= j) {
                PrevisaoApi aux = v.getElemento(i);
                v.insereNaPosicao(v.getElemento(j), i); // Esta linha está causando o erro
                v.insereNaPosicao(aux, j);

                i++;
                j--;
            }
        }

        if (indInicio < j) {
            ordenarPrevisao(v, indInicio, j, desc, tipo);
        }

        if (i < indFim) {
            ordenarPrevisao(v, i, indFim, desc, tipo);
        }
    }


    public  int pesquisaBinaria(ListaObj<PrevisaoApi> vetor, LocalDateTime valor) {
        int inicio = 0;
        int fim = vetor.getTamanho() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            LocalDateTime dataHoraMeio =  vetor.getElemento(meio).getDataHora();


            int comparacao = dataHoraMeio.compareTo(valor);

            // Se a data e hora na posição do meio for igual ao valor buscado, retorna o índice
            if (comparacao == 0) {
                return meio;
            }
            // Se a data e hora na posição do meio for menor que o valor buscado, ajusta o início
            else if (comparacao < 0) {
                inicio = meio + 1;
            }
            // Se a data e hora na posição do meio for maior que o valor buscado, ajusta o fim
            else {
                fim = meio - 1;
            }
        }

        // Se não encontrar a data e hora na lista, retorna -1
        return -1;
    }
}
