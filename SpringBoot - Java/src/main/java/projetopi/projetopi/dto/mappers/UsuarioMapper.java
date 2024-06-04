package projetopi.projetopi.dto.mappers;

import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.response.BarbeiroConsulta;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.Endereco;

import java.util.ArrayList;
import java.util.List;

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

    public static List<BarbeiroConsulta> toDto(List<Barbeiro> barbeiros){

        List<BarbeiroConsulta> dtos = new ArrayList<>();
        for (Barbeiro b: barbeiros){
            dtos.add(new BarbeiroConsulta(b));
        }
        return dtos;
    }
}
