package projetopi.projetopi.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetopi.projetopi.dto.request.CadastroCliente;
import projetopi.projetopi.dto.response.BarbeiroConsulta;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Cliente;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.service.ImageService;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioMapper {

    @Autowired
    private ImageService imageService;

    public  Endereco toDtoEndereco(CadastroCliente c) {
        return new Endereco(
            c.getCep(),
            c.getLogradouro(),
            c.getNumero(),
            c.getComplemento(),
            c.getEstado(),
            c.getCidade()
        );
    }

    public  Cliente toDto(CadastroCliente c) {

        return new Cliente(
                c.getNome(),
                c.getEmail(),
                c.getSenha(),
                c.getCelular(),
                toDtoEndereco(c),
                c.getUsername()
        );

    }

    public  List<BarbeiroConsulta> toDto(List<Barbeiro> barbeiros){

        List<BarbeiroConsulta> dtos = new ArrayList<>();
        for (Barbeiro b: barbeiros){

            dtos.add(new BarbeiroConsulta(b,
                    imageService.getImgURL(
                    b.getImgPerfil() == null ? null :
                    b.getImgPerfil(), "usuario")));
        }
        return dtos;
    }

}
