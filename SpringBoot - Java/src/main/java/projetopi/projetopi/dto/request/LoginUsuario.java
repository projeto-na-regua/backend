package projetopi.projetopi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public class LoginUsuario {

    @Email
    private String email;

    private String senha;

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}
