package projetopi.projetopi.dto.mappers;

import projetopi.projetopi.dominio.api.Dado;
import projetopi.projetopi.dominio.api.Precipitacao;
import projetopi.projetopi.dominio.api.Temperatura;
import projetopi.projetopi.dto.response.PrevisaoApi;

public class PrevisaoMapper {

    public PrevisaoApi[] toDto(Temperatura t, Precipitacao p){

        PrevisaoApi[] dto = new PrevisaoApi[t.getTemperatura().size()];

        for (int i = 0; i < dto.length; i++) {
            dto[i] = new PrevisaoApi(t, p, i);
        }

        return dto;
    }
}
