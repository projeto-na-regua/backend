package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Especialidade;
import projetopi.projetopi.repositorio.EspecialidadeRepository;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;


@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @GetMapping
    public ResponseEntity<List<Especialidade>> getEspecialidades(){
        List<Especialidade> especialidades = especialidadeRepository.findAll();
        return especialidades.isEmpty() ? status(204).build() : status(200).body(especialidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidade> especialidade (@PathVariable Integer id){
        return of(especialidadeRepository.findById(id));
    }


}
