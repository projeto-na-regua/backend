package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.*;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.InfoBarbearia;
import projetopi.projetopi.dto.response.InfoUsuario;
import projetopi.projetopi.dto.response.TokenConsulta;
import projetopi.projetopi.repositorio.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.service.UsuarioService;
import projetopi.projetopi.util.Token;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.status;
@CrossOrigin("*")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    private ResponseEntity<TokenConsulta> login(@Valid @RequestBody LoginUsuario loginUsuario){
        TokenConsulta token = service.loginIsValid(loginUsuario);
        return token == null ? status(404).build() : status(200).body(token);
    }

    @PostMapping("/cadastro") // CADASTRO CLIENTE
    private ResponseEntity<TokenConsulta> cadastrarCliente(@Valid @RequestBody CadastroCliente c){

        if(service.usuarioExistsByEmail(c.getEmail())){
            return status(409).build();

        }
        return status(201).body(service.cadastrarCliente(c));
    }

    @PostMapping("/cadastro-barbearia") // CADASTRO BARBEIRO
    private ResponseEntity<TokenConsulta> cadastrarBarbeiro(@Valid @RequestBody CadastroBarbearia nvBarbearia){
        if(service.usuarioExistsByEmail(nvBarbearia.getEmail())){
            return status(409).build();

        }
        return status(201).body(service.cadastrarBarbeiro(nvBarbearia));
    }


    @PutMapping("/editar-perfil")
    private ResponseEntity<InfoUsuario> editarUsuario(@RequestHeader("Authorization") String token, @Valid @RequestBody InfoUsuario nvUsuario){

        Integer id = service.getUserId(token);

        if (!service.usuarioExistsById(id)){
            return status(404).build();
        }

        return status(200).body(service.editarUsuario(id, nvUsuario));

    }

    @GetMapping("/perfil")
    private ResponseEntity<List<InfoUsuario>> getUsuario(@RequestHeader("Authorization") String token){
        return ok(service.getUsuario(token));
    }



}
