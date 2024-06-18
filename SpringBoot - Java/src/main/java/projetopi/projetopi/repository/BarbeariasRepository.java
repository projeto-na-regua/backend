package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.dto.response.BarbeariaAvaliacao;
import projetopi.projetopi.dto.response.BarbeariaServico;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.dto.response.BarbeariaConsulta;
import projetopi.projetopi.dto.response.EnderecoConsulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BarbeariasRepository extends JpaRepository<Barbearia, Integer> {


//      @Query(value = "SELECT DISTINCT b.img_perfil, b.id_barbearia " +
//              "FROM barbearia b " +
//              "JOIN agendamento a ON b.id_barbearia = a.barbearia_id_barbearia " +
//              "WHERE a.status = 'Concluido' AND a.cliente_id_usuario = 20",
//              nativeQuery = true)
//      List<BarbeariaConsulta> findImagensBarbeariaAgendamentoConcluido(Integer id);

      @Query("SELECT new Barbearia(b.nomeNegocio, b.emailNegocio, b.celularNegocio, b.cnpj, b.cpf, b.descricao) FROM Barbearia b WHERE b.id = :id")
      List<BarbeariaConsulta> findByInfoBarbearia(@Param("id") Integer id);

      @Query("SELECT new Barbearia(b.endereco) FROM Barbearia b WHERE b.id = :id")
      List<EnderecoConsulta> findByInfoEndereco(@Param("id") Integer id);

      Barbearia findByCpf(String cpf);

      List<Barbearia> findByNomeNegocioContaining(String nomeNegocio);

      Barbearia findByNomeNegocio(String nomeNegocio);

      @Query("SELECT new projetopi.projetopi.dto.response.BarbeariaServico(b, s) FROM Barbearia b JOIN b.servicos s WHERE s.tipoServico LIKE %?1%")
      List<BarbeariaServico> findBarbeariasByTipoServico(String tipoServico);

      @Query("SELECT new projetopi.projetopi.dto.response.BarbeariaAvaliacao(b.nomeNegocio, AVG(av.resultadoAvaliacao) AS media, b.imgPerfil) " +
              "FROM Barbearia b " +
              "JOIN Agendamento a ON b.id = a.barbearia.id " +
              "JOIN Avaliacao av ON av.id = a.avaliacao.id " +
              "GROUP BY b.id, b.nomeNegocio, b.imgPerfil " +
              "ORDER BY media DESC")
      List<BarbeariaAvaliacao> findTopBarbearias();
}

//      @Query("SELECT b FROM Barbearia b WHERE " +
//              "distance(geography::Point(:latitude, :longitude, 4326), b.endereco.location) <= :raio")
//      List<Barbearia> encontrarBarbeariasProximas(@Param("latitude") Double latitude,
//                                                  @Param("longitude") Double longitude,
//                                                  @Param("raio") Double raio);
