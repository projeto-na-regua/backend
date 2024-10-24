package projetopi.projetopi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projetopi.projetopi.entity.Usuario;

import java.util.List;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


    @Query(value = "SELECT * FROM usuario WHERE dtype = 'barbeiro' AND usuario_fk_barbearia = :idBarbearia", nativeQuery = true)
    List<Usuario> findAllByBarbeariaId(Integer idBarbearia);
    Usuario findByEmail(String email);
    Usuario findByUsername(String username);

    Usuario findByEmailAndSenha(String email, String senha);

    @Query("select u.barbearia from Usuario u WHERE u.id = ?1")
    Usuario getBarbearia(Integer id);

    @Query("select u.dtype from Usuario u WHERE u.id = ?1")
    String getDtypeById(Integer id);
}
