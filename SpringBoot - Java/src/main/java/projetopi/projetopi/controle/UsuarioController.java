package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;

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

    @Autowired
    private DiaSemanaRepository diaSemanaRepository;

    @PostMapping("/cadastro") // CADASTRO CLIENTE
    private ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody CadastroCliente c){
        Integer idEdereco = enderecoRepository.save(c.gerarEndereco()).getId();
        Cliente cliente = c.gerarCliente();

        cliente.setEndereco(enderecoRepository.getReferenceById(idEdereco));
        clienteRepository.save(cliente);

        return status(201).body(cliente);
    }

    @PostMapping("/cadastro-barbearia") // CADASTRO BARBEIRO
    private ResponseEntity<CadastroBarbearia> cadastrarBarbeiro(@Valid @RequestBody CadastroBarbearia nvBarbearia){

        Endereco endereco = nvBarbearia.gerarEndereco();
        Barbearia barbearia = nvBarbearia.gerarBarbearia();
        Barbeiro barbeiro = nvBarbearia.gerarBarbeiro();
        DiaSemana[] diaSemana = nvBarbearia.gerarSemena();

        Integer idEndereco = enderecoRepository.save(endereco).getId();

        barbearia.setEndereco(enderecoRepository.getReferenceById(idEndereco));

        Integer idBarbearia = barbeariasRepository.save(barbearia).getId();

        barbeiro.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));

        barbeiroRepository.save(barbeiro);

        for (DiaSemana d: diaSemana){
            d.setBarbearia(barbeariasRepository.getReferenceById(idBarbearia));
            diaSemanaRepository.save(d);
        }

        return status(201).body(nvBarbearia);
    }

    //LOGIN E LOGOUT AQUI

}
