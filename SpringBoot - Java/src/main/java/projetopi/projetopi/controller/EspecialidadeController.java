package projetopi.projetopi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.entity.Especialidade;
import projetopi.projetopi.repository.EspecialidadeRepository;
import projetopi.projetopi.service.EspecialidadeService;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    EspecialidadeService service;

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
