package projetopi.projetopi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projetopi.projetopi.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


    Usuario findByEmail(String email);

    Usuario findByEmailAndSenha(String email, String senha);

    @Query("select u.barbearia from Usuario u WHERE u.id = ?1")
    Usuario getBarbearia(Integer id);
}
