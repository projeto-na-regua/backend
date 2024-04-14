package projetopi.projetopi.controle;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.DiaSemana;
import projetopi.projetopi.dominio.Endereco;
import projetopi.projetopi.repositorio.BarbeariasRepository;
import projetopi.projetopi.repositorio.DiaSemanaRepository;
import projetopi.projetopi.repositorio.EnderecoRepository;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/barbearia")
public class BarbeariaController {


    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @Autowired
    private DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<Barbearia> getPerfil(@PathVariable Integer id){
        return of(barbeariasRepository.findById(id));
    }

    @PutMapping("/perfil/endereco/{id}")
    public ResponseEntity<Barbearia> editarPefil(@PathVariable Integer id, @Valid @RequestBody Endereco end){

        if (!barbeariasRepository.existsById(id)){
            return status(404).build();
        }

        Barbearia b = barbeariasRepository.findById(id).get();
        Integer idEnd = b.getEndereco().getId();

        end.setId(idEnd);
        enderecoRepository.save(end);
        return status(200).body(b);

    }

    @PutMapping("/perfil/info/{id}")
    public ResponseEntity<Barbearia> editarPerfilInfo(@PathVariable Integer id, @Valid @RequestBody Barbearia nvBarbearia){

        if (!barbeariasRepository.existsById(id)){
            return status(404).build();
        }

        Barbearia b = barbeariasRepository.findById(id).get();
        nvBarbearia.setEndereco(b.getEndereco());
        nvBarbearia.setId(id);
        barbeariasRepository.save(nvBarbearia);
        return status(200).body(nvBarbearia);
    }

    @PutMapping("perfil/horario-comercial/{id}")
    public ResponseEntity<DiaSemana[]> editarHorarioComercial(@PathVariable Integer id, @RequestBody DiaSemana[] hrNovos){

        if (!barbeariasRepository.existsById(id)){
            return status(404).build();
        }

        DiaSemana[] hrAntigos = diaSemanaRepository.findByBarbeariaId(id);

        for (int i = 0; i < hrNovos.length; i++) {
            if (hrAntigos.length > i) {
                hrNovos[i].setId(hrAntigos[i].getId());
            }

            hrNovos[i].setBarbearia(barbeariasRepository.getReferenceById(id));
            diaSemanaRepository.save(hrNovos[i]);
        }

        return status(200).body(hrNovos);
    }

}
