package projetopi.projetopi.controle;


import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.metadata.HsqlTableMetaDataProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.repositorio.BarbeiroRepository;

import java.util.List;

@RestController
@RequestMapping("/management")
public class BarbeariaController {

        @Autowired
        private BarbeiroRepository barbeiroRepository;

        @GetMapping("/funcionarios/{fk}")
        public ResponseEntity<List<Barbeiro>> getFuncionarios(@PathVariable Integer fk){
            var barbeiros = barbeiroRepository.findByBarbeariaId(fk);
            return barbeiros.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.status(200).body(barbeiros);
        }


}

