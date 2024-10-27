package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.Comentario;
import projetopi.projetopi.entity.Midia;
import projetopi.projetopi.entity.Postagem;

import java.util.List;

public interface MidiaRepository extends JpaRepository<Midia, Integer> {

    // Método para encontrar mídias por ID da postagem
    List<Midia> findByPostagem(Postagem postagem);

    // Método para encontrar mídias por ID do comentário
    List<Midia> findByComentario(Comentario comentario);

}
