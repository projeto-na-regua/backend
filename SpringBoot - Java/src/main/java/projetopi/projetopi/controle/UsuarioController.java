package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.repositorio.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BarbeariasRepository barbeariasRepository;

    @PostMapping() // CADASTRO CLIENTE
    private ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody Cliente c){
        enderecoRepository.save(c.getEndereco());
        clienteRepository.save(c);
        return status(201).body(c);
    }

    @PostMapping("/barbeiros") // CADASTRO BARBEIRO
    private ResponseEntity<Barbeiro> cadastrarBarbeiro(@Valid @RequestBody Barbeiro nvBarberio){

        var barbearias = barbeariasRepository.findAll();

        for (Barbearia b: barbearias) {

            if(barbearias.equals(nvBarberio.getBarbearia())){
                barbeiroRepository.save(nvBarberio);
            }

        }

        enderecoRepository.save(nvBarberio.getBarbearia().getEndereco());
        barbeariasRepository.save(nvBarberio.getBarbearia());
        barbeiroRepository.save(nvBarberio);
        return status(201).body(nvBarberio);
    }

}
