package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.Endereco;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    @Query(value = "SELECT e FROM Endereco e " +
            "ORDER BY function('ST_Distance_Sphere', e.localizacao, function('ST_GeomFromText', :point)) ASC")
    List<Endereco> findAllOrderByDistance(@Param("point") String point);
}
