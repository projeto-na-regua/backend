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

      @Query("SELECT b FROM Barbearia b JOIN b.servicos s WHERE s.tipoServico LIKE %?1%")
      List<Barbearia> findBarbeariasByTipoServico(String tipoServico);

      @Query("SELECT new projetopi.projetopi.dto.response.BarbeariaAvaliacao(b.nomeNegocio, AVG(av.resultadoAvaliacao) AS media, b.imgPerfil) " +
              "FROM Barbearia b " +
              "JOIN Agendamento a ON b.id = a.barbearia.id " +
              "JOIN Avaliacao av ON av.id = a.avaliacao.id " +
              "GROUP BY b.id, b.nomeNegocio, b.imgPerfil " +
              "ORDER BY media DESC")
      List<BarbeariaAvaliacao> findTopBarbearias();

      @Query(value = "SELECT b FROM Barbearia b " +
              "JOIN b.endereco e " +
              "WHERE ST_Distance_Sphere(e.localizacao, ST_GeomFromText(:ponto, 4326)) <= :raio")
      List<Barbearia> findBarbeariasProximas(@Param("ponto") String ponto, @Param("raio") Double raio);

      @Query(value = "SELECT DISTINCT b.*, AVG(a.resultado_avaliacao) AS media_avaliacao " +
              "FROM barbearia b " +
              "JOIN endereco e ON b.barbearia_fk_endereco = e.id_endereco " +
              "JOIN servico s ON b.id_barbearia = s.servico_fk_barbearia " +
              "JOIN dia_semana ds ON b.id_barbearia = ds.ds_id_barbearia " +
              "LEFT JOIN agendamento ag ON b.id_barbearia = ag.barbearia_id_barbearia " +
              "LEFT JOIN avaliacao a ON ag.ag_fk_avaliacao = a.id_avaliacao " +
              "WHERE (:ponto IS NULL OR ST_Distance_Sphere(e.localizacao, ST_GeomFromText(:ponto, 4326)) <= :raio) " +
              "AND (:tipoServico IS NULL OR LOWER(s.tipo_servico) LIKE LOWER(CONCAT('%', :tipoServico, '%'))) " +
              "AND (:diaSemana IS NULL OR ds.nome = :diaSemana) " +
              "AND (:hora IS NULL OR (ds.hora_abertura <= :hora AND ds.hora_fechamento >= :hora)) " +
              "AND ds.hora_abertura IS NOT NULL " +
              "AND ds.hora_fechamento IS NOT NULL " +
              "GROUP BY b.id_barbearia",
              nativeQuery = true)
      List<Object[]> findBarbeariasProximasByTipoServicoEDisponibilidadeComMedia(
              @Param("ponto") String ponto,
              @Param("raio") Double raio,
              @Param("tipoServico") String tipoServico,
              @Param("diaSemana") String diaSemana,
              @Param("hora") LocalTime hora);




}


