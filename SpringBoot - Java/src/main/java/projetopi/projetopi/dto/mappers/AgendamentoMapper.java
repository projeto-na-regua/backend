package projetopi.projetopi.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import projetopi.projetopi.dto.request.AgendamentoCriacao;
import projetopi.projetopi.dto.response.AgendamentoConsulta;
import projetopi.projetopi.entity.*;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.BarbeiroRepository;
import projetopi.projetopi.repository.ClienteRepository;
import projetopi.projetopi.repository.ServicoRepository;

public class AgendamentoMapper {

    @Autowired
    private static BarbeiroRepository barbeiroRepository;

    @Autowired
    private static ServicoRepository servicoRepository;

    @Autowired
    private static ClienteRepository clienteRepository;

    @Autowired
    private static BarbeariasRepository barbeariasRepository;

    public static AgendamentoConsulta toDto(Agendamento a){
        return new AgendamentoConsulta(a.getId(), a.getStatus(),a.getDataHora(),
                                       a.getServico().getTipoServico(),
                                       a.getServico().getDescricao(),
                                       a.getServico().getPreco(),
                                       a.getCliente().getNome(),
                                       a.getBarbeiro().getNome(),
                                       a.getBarbearia().getNomeNegocio(),
                                       a.getBarbearia().getEndereco());
    }

    public static Agendamento toEntity(AgendamentoCriacao a){
        Servico servico = servicoRepository.findById(a.getIdServico()).get();
        Barbeiro barbeiro = barbeiroRepository.findById(a.getIdBarbeiro()).get();
        Barbearia barbearia = barbeariasRepository.findById(a.getIdBarbearia()).get();
        return new Agendamento(a.getDataHora(), servico, barbeiro,  barbearia);
    }
}
