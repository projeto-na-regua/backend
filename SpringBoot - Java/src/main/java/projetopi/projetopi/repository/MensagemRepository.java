package projetopi.projetopi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetopi.projetopi.entity.Mensagem;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {


        Mensagem findFirstByChat_IdOrderByDataCriacaoDesc(Integer chatId);

        List<Mensagem> findByChat_IdOrderByDataCriacaoDesc(Integer chatId);


}
