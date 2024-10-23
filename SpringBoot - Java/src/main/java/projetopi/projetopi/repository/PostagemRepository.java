package projetopi.projetopi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Integer> {
}
