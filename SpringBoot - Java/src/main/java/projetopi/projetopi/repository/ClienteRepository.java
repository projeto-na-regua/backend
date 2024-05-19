package projetopi.projetopi.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.dto.response.UsuarioConsulta;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("select new Cliente(c.nome, c.email, c.celular) from Cliente c where c.id = :id")
    List<UsuarioConsulta> findByInfoUsuario(@Param("id") Integer id);


    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.dtype = 'Barbeiro', u.barbearia = :novaBarbearia, u.adm = :novoAdm WHERE u.id = :clienteId")
    void atualizarClienteParaBarbeiro(@Param("clienteId") Integer clienteId,
                                      @Param("novaBarbearia") Barbearia novaBarbearia,
                                      @Param("novoAdm") boolean novoAdm);

    Cliente findByEmail(String email);

    List<Cliente> findByEmailStartingWith(String email);



}
