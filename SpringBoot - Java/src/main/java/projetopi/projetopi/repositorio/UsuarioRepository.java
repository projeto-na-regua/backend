package projetopi.projetopi.repositorio;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Cliente;
import projetopi.projetopi.dominio.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


    Usuario findByEmail(String email);

    Usuario findByEmailAndSenha(String email, String senha);

    @Query("select u.barbearia from Usuario u WHERE u.id = ?1")
    Usuario getBarbearia(Integer id);
}
