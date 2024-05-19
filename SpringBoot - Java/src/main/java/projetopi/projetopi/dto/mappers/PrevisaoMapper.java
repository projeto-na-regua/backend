package projetopi.projetopi.dto.mappers;

import projetopi.projetopi.entity.api.Precipitacao;
import projetopi.projetopi.entity.api.Temperatura;
import projetopi.projetopi.dto.response.PrevisaoApi;
import projetopi.projetopi.util.ListaObj;

public class PrevisaoMapper {

    public static ListaObj<PrevisaoApi> toDto(Temperatura t, Precipitacao p){

        int tamanhoVetor = Math.min(t.getTemperatura().size(), p.getPrecipitacao().size());


        ListaObj<PrevisaoApi> dto = new ListaObj<>(tamanhoVetor + 10);

        for (int i = 0; i < tamanhoVetor; i++) {
            dto.adiciona(new PrevisaoApi(t, p, i));
        }

        return dto;
    }

}
