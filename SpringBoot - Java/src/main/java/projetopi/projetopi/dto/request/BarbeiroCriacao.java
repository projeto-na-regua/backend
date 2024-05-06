package projetopi.projetopi.dto.request;

import com.azure.resourcemanager.storage.models.PrivateEndpoint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BarbeiroCriacao {

    @NotBlank
    private String nome;

    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 12)
    private String senha;

    @NotBlank
    @Size(max = 15)
    private String celular;

    public BarbeiroCriacao() {
    }
}
