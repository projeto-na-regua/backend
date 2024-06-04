package projetopi.projetopi.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.Endereco;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastroCliente {

    @NotBlank
    @Size(min = 5, max = 120)
    private String nome;

    @Email
    @NotBlank
    @Column(name = "email")
    private String email;

    @Size(min = 8)
    @NotBlank
    private String senha;

    @NotBlank
    @Size(max = 15)
    private String celular;

    @Size(max = 9)
    private String cep;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String cidade;

    private String estado;

}
