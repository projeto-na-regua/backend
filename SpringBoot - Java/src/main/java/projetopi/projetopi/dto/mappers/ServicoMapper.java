package projetopi.projetopi.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import projetopi.projetopi.dto.request.ServicoCriacao;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Servico;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeiroRepository;

public class ServicoMapper {


    public static Servico toEntity(ServicoCriacao nvServico){
            Servico servico = new Servico();
            servico.setPreco(nvServico.getPreco());
            servico.setDescricao(nvServico.getDescricao());
            servico.setTempoEstimado(nvServico.getTempoEstimado());
            servico.setTipoServico(nvServico.getTipoServico());
            servico.setBarbeiro(new Barbeiro(nvServico.getBarbeiro()));
            return servico;
    }
}
