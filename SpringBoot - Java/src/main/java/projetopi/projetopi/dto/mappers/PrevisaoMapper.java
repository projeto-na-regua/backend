package projetopi.projetopi.dto.mappers;

import projetopi.projetopi.dominio.api.Dado;
import projetopi.projetopi.dominio.api.Precipitacao;
import projetopi.projetopi.dominio.api.Temperatura;
import projetopi.projetopi.dto.response.PrevisaoApi;
import projetopi.projetopi.util.ListaObj;

public class PrevisaoMapper {

    public ListaObj<PrevisaoApi> toDto(Temperatura t, Precipitacao p){

        int tamanhoVetor = 0;

        if (t.getTemperatura().size() <= p.getPrecipitacao().size()){
            tamanhoVetor = t.getTemperatura().size();
        }

        if (t.getTemperatura().size() > p.getPrecipitacao().size()){
            tamanhoVetor = p.getPrecipitacao().size();
        }

        ListaObj<PrevisaoApi> dto = new ListaObj<>(tamanhoVetor);

        for (int i = 0; i < dto.getTamanho(); i++) {
            dto.adiciona(new PrevisaoApi(t, p, i));
        }

        return dto;
    }
}
