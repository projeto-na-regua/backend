package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Servico;
import projetopi.projetopi.dto.response.ServicoConsulta;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    List<Servico>  findByBarbeariaIdAndStatus(Integer barbeariaId, Boolean status);

    List<Servico>  findByBarbeariaId(Integer barbeariaId);

//    @Query("select new Servico(s.id, s.preco, s.descricao, s.tipoServico, s.tempoEstimado, s.barbeiro.nome) from Servico s where s.barbearia.id = ?1")
//    List<ServicoConsulta> findByInfoServicoBarbearia(Integer id);

//  @Query("select new Servico(s.id, s.preco, s.descricao, s.tipoServico, s.tempoEstimado, s.barbeiro.nome) from Servico s where s.id = ?1")
    Servico findByBarbeariaIdAndId (Integer barbeariaId, Integer id);


}
