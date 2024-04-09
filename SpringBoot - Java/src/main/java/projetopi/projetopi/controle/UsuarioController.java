package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.util.CadastroBarbearia;

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

    @PostMapping("/cadastro") // CADASTRO CLIENTE
    private ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody Cliente c){
        Integer idEdereco = enderecoRepository.save(c.getEndereco()).getId();
        c.setEndereco(enderecoRepository.getReferenceById(idEdereco));
        clienteRepository.save(c);
        return status(201).body(c);
    }

    @PostMapping("/cadastro-barbearia") // CADASTRO BARBEIRO
    private ResponseEntity<CadastroBarbearia> cadastrarBarbeiro(@Valid @RequestBody CadastroBarbearia nvBarbearia){

        Endereco endereco = nvBarbearia.getBarbearia().getEndereco();
        Barbearia barbearia = nvBarbearia.getBarbearia();
        Barbeiro barbeiro = nvBarbearia.getBarbeiro();

        Integer idEndereco = enderecoRepository.save(endereco).getId();

        barbearia.setEndereco(enderecoRepository.getReferenceById(idEndereco));

        Integer idBarbearia = barbeariasRepository.save(barbearia).getId();


        barbeiro.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));
        barbeiro.setAdm(true);

        barbeiroRepository.save(barbeiro);

        return status(201).body(nvBarbearia);

    }

    //LOGIN E LOGOUT AQUI

}
