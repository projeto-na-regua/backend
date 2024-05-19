package projetopi.projetopi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.request.LoginUsuario;
import projetopi.projetopi.dto.response.DtypeConsulta;
import projetopi.projetopi.dto.response.ImgConsulta;
import projetopi.projetopi.dto.response.PerfilUsuarioConsulta;
import projetopi.projetopi.dto.response.UsuarioConsulta;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.service.UsuarioService;

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
        return status(200).body(service.loginIsValid(loginUsuario));
    }

    @PostMapping("/cadastro") // CADASTRO CLIENTE
    private ResponseEntity<String> cadastrarCliente(@Valid @RequestBody CadastroCliente c){
        return status(201).body(service.cadastrarCliente(c));
    }

    @PostMapping("/cadastro-barbearia") // CADASTRO BARBEIRO
    private ResponseEntity<Barbearia> cadastrarBarbeiro(@RequestHeader("Authorization") String token,
                                                        @Valid @RequestBody CadastroBarbearia nvBarbearia){
        return status(201).body(service.cadastrarBarbeiro(nvBarbearia, token));
    }


    @PutMapping("/editar-perfil")
    private ResponseEntity<UsuarioConsulta> editarUsuario(@RequestHeader("Authorization") String token,
                                                          @Valid @RequestBody UsuarioConsulta nvUsuario){
        return status(200).body(service.editarUsuario(token, nvUsuario));
    }

    @PutMapping("/editar-img-perfil")
    public ResponseEntity<ImgConsulta> uploadFile(@RequestHeader("Authorization") String token,
                                                  @RequestParam("file") MultipartFile file) {
        return service.editarImgPerfil(token, file);
    }

    @GetMapping("/get-image")
    public ResponseEntity<ByteArrayResource> getImage(@RequestHeader("Authorization") String token){
        return service.getImage(token);
    }

    @GetMapping("/perfil")
    private ResponseEntity<PerfilUsuarioConsulta> getUsuario(@RequestHeader("Authorization") String token){
        return ok(service.getPerfil(token));
    }

    @GetMapping("/user")
    private ResponseEntity<DtypeConsulta> getUserById(@RequestHeader("Authorization") String token){
        return ok(service.getUsuario(token));
    }

}
