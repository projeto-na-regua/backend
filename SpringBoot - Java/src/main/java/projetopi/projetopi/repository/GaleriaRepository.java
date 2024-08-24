package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.ImgsGaleria;

import java.util.List;

public interface GaleriaRepository extends JpaRepository<ImgsGaleria, Integer> {


    List<ImgsGaleria> findByClienteIdAndIsActiveTrueOrIsActiveIsNull(Integer clienteId);

}
