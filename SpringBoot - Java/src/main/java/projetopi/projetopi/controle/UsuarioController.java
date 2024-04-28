package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.response.InfoBarbearia;
import projetopi.projetopi.dto.response.InfoUsuario;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.util.Token;

import java.util.List;

import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.status;
@CrossOrigin("*")
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

    @Autowired
    private UsuarioRepository usuarioRepository;


    private Token token;

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


    @PutMapping("/perfil/{id}")
    private ResponseEntity<InfoUsuario> editarUsuario(@PathVariable Integer id, @Valid @RequestBody InfoUsuario nvUsuario){

        if(!usuarioRepository.existsById(id)){
            return status(404).build();
        }

        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            Cliente usuario = nvUsuario.gerarCliente();
            usuario.setId(clienteRepository.getReferenceById(id).getId());
            usuario.setSenha(usuarioRepository.getReferenceById(id).getSenha());
            usuarioRepository.save(usuario);
            return status(200).body(nvUsuario);

        }else{
            Barbeiro usuario = nvUsuario.gerarBarberiro();
            usuario.setId(usuarioRepository.getReferenceById(id).getId());
            usuario.setSenha(usuarioRepository.getReferenceById(id).getSenha());
            usuarioRepository.save(usuario);
            return status(200).body(nvUsuario);
        }
    }

    @GetMapping("/perfil/{id}")
    private ResponseEntity<List<InfoUsuario>> getUsuario(@PathVariable Integer id){

        if(!usuarioRepository.existsById(id)){
            return status(404).build();
        }

        if(usuarioRepository.getReferenceById(id).getDtype().equals("Cliente")){
            return status(200).body(clienteRepository.findByInfoUsuario(id));

        }else{
            return status(200).body(barbeiroRepository.findByInfoUsuario(id));
        }

    }



    //LOGIN E LOGOUT AQUI

    @PostMapping("/gerar-token")
    public ResponseEntity<Usuario> post(@RequestBody Usuario usuario){
        usuarioRepository.save(usuario);
        return status(201).body(usuario);
    }

    @GetMapping("/get-token/{nome}")
    public ResponseEntity<String> getTokenPeloUserName(@PathVariable String nome){
        var user = usuarioRepository.findByNome(nome);


        return user.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(token.getToken(user.get(0)));
    }

    @GetMapping("/get-id-by-token/{token}")
    public ResponseEntity<Integer> getIdByToken(@PathVariable String tokenString){

        var idUser = token.getUserIdByToken(tokenString);
        var user = usuarioRepository.findById(idUser);


        return user.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(token.getUserIdByToken(tokenString));
    }

}
