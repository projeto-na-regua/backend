package projetopi.projetopi.controle;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.DtypeConsulta;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.service.UsuarioService;

import java.io.IOException;
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
    private ResponseEntity<String> login(@Valid @RequestBody LoginUsuario loginUsuario){
        String token = service.loginIsValid(loginUsuario);
        return token == null ? status(404).build() : status(200).body(token);
    }

    @PostMapping("/cadastro") // CADASTRO CLIENTE
    private ResponseEntity<String> cadastrarCliente(@Valid @RequestBody CadastroCliente c){

        if(service.usuarioExistsByEmail(c.getEmail())){
            return status(409).build();

        }
        return status(201).body(service.cadastrarCliente(c));
    }

    @PostMapping("/cadastro-barbearia") // CADASTRO BARBEIRO
    private ResponseEntity<String> cadastrarBarbeiro(@RequestHeader("Authorization") String token, @Valid @RequestBody CadastroBarbearia nvBarbearia){
        if(service.usuarioExistsByEmail(nvBarbearia.getEmail())){
            return status(409).build();

        }
        return status(201).body(service.cadastrarBarbeiro(nvBarbearia));
    }


    @PutMapping("/editar-perfil")
    private ResponseEntity<UsuarioConsulta> editarUsuario(@RequestHeader("Authorization") String token, @Valid @RequestBody UsuarioConsulta nvUsuario){
        Integer id = service.getUserId(token);

        if (!service.usuarioExistsById(id)){
            return status(404).build();
        }

        return status(200).body(service.editarUsuario(id, nvUsuario));
    }

    @PutMapping("/editar-img-perfil")
    public ResponseEntity<ImgConsulta> uploadFile(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        if (!service.usuarioExiste(token)) return status(404).build();
        return service.editarImgPerfil(token, file);
    }

    @GetMapping("/get-image")
    public ResponseEntity<ByteArrayResource> getImage(@RequestHeader("Authorization") String token){
        if (!service.usuarioExiste(token)) return status(404).build();
        return service.getImage(token);
    }


    @GetMapping("/perfil")
    private ResponseEntity<UsuarioConsulta> getUsuario(@RequestHeader("Authorization") String token){
        return ok(service.getPerfil(token));
    }

    @GetMapping("/user")
    private ResponseEntity<DtypeConsulta> getUserById(@RequestHeader("Authorization") String token){
        return ok(service.getUsuario(token));
    }


}
