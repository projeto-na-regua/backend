package projetopi.projetopi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetopi.projetopi.dominio.AgendaAux;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<AgendaAux, Integer> {
    List<AgendaAux> findAllByNomeCliente(String nomeCliente);

    List<AgendaAux> findAllByNomeBarbeiro(String nomeBarbeiro);

    List<AgendaAux> findAllByNomeBarbearia(String nomeBarbearia);

}

