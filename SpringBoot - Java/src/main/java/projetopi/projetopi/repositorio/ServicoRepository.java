package projetopi.projetopi.repositorio;

import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.Servico;
import projetopi.projetopi.dto.response.InfoServico;
import projetopi.projetopi.dto.response.InfoUsuario;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    List<Servico>  findByBarbeariaId(Integer barbeariaId);

    @Query("select new Servico(s.preco, s.descricao, s.tipoServico, s.tempoEstimado, s.barbeiro.nome) from Servico s where s.barbearia.id = ?1")
    List<InfoServico> findByInfoServicoBarbearia(Integer id);

    @Query("select new Servico(s.preco, s.descricao, s.tipoServico, s.tempoEstimado, s.barbeiro.nome) from Servico s where s.id = ?1")
    List<InfoServico>  findByInfoServico (Integer id);

}