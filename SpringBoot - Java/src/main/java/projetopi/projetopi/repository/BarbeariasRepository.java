package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.dto.response.BarbeariaConsulta;
import projetopi.projetopi.dto.response.EnderecoConsulta;

import java.util.List;

public interface BarbeariasRepository extends JpaRepository<Barbearia, Integer> {



      @Query("SELECT new Barbearia(b.nomeNegocio, b.emailNegocio, b.celularNegocio, b.cnpj, b.cpf, b.descricao) FROM Barbearia b WHERE b.id = :id")
      List<BarbeariaConsulta> findByInfoBarbearia(@Param("id") Integer id);

      @Query("SELECT new Barbearia(b.endereco) FROM Barbearia b WHERE b.id = :id")
      List<EnderecoConsulta> findByInfoEndereco(@Param("id") Integer id);

      Barbearia findByCpf(String cpf);

      Barbearia findByNomeNegocio(String nomeNegocio);

//      @Query("SELECT b FROM Barbearia b WHERE " +
//              "distance(geography::Point(:latitude, :longitude, 4326), b.endereco.location) <= :raio")
//      List<Barbearia> encontrarBarbeariasProximas(@Param("latitude") Double latitude,
//                                                  @Param("longitude") Double longitude,
//                                                  @Param("raio") Double raio);
}