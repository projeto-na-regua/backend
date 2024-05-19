package projetopi.projetopi.dto.mappers;

import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.Endereco;

public class UsuarioMapper {

    public static Endereco toDtoEndereco(CadastroCliente c) {
        return new Endereco(
            c.getCep(),
            c.getLogradouro(),
            c.getNumero(),
            c.getComplemento(),
            c.getEstado(),
            c.getCidade()
        );

    }

    public static Cliente toDto(CadastroCliente c) {
        return new Cliente(
                c.getNome(),
                c.getEmail(),
                c.getSenha(),
                c.getCelular(),
                toDtoEndereco(c)
        );

    }
}
