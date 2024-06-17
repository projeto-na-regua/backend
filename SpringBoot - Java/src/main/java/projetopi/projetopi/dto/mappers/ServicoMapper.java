package projetopi.projetopi.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetopi.projetopi.dto.request.ServicoCriacao;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Servico;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeiroRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServicoMapper {


    public static Servico toEntity(ServicoCriacao nvServico){
            List<Barbeiro> barbeiros = new ArrayList<>();
//            barbeiros.add(new Barbeiro(nvServico.getBarbeiro()));
            Servico servico = new Servico();
            servico.setPreco(nvServico.getPreco());
            servico.setDescricao(nvServico.getDescricao());
            servico.setTempoEstimado(nvServico.getTempoEstimado());
            servico.setTipoServico(nvServico.getTipoServico());
//            servico.setBarbeiros(barbeiros);
            return servico;
    }

    public static ServicoConsulta toDto(Servico servico){
        return new ServicoConsulta(servico);
    }

    public List<ServicoConsulta> toDto(List<Servico> servicos){
        List<ServicoConsulta> servicoConsultas = new ArrayList<>();
        for (Servico s: servicos){
            servicoConsultas.add(new ServicoConsulta(s));
        }
        return servicoConsultas;
    }
}
