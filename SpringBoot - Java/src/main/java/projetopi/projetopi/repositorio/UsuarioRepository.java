package projetopi.projetopi.repositorio;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projetopi.projetopi.dominio.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByNome(String nome);

    Usuario findByEmailAndSenha(String email, String senha);

    Usuario findByEmail(String email);

}
