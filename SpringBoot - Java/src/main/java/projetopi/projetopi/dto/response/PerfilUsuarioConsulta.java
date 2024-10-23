package projetopi.projetopi.dto.response;

import lombok.Getter;
import lombok.Setter;
import projetopi.projetopi.entity.Endereco;
import projetopi.projetopi.entity.Usuario;

@Getter
@Setter
public class PerfilUsuarioConsulta {

    private String nome;

    private String email;

    private String senha;

    private String celular;

    private String imgPerfil;

    private String cep;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String cidade;

    private String estado;

    private String username;

    public PerfilUsuarioConsulta(Usuario u) {
        this.nome = u.getNome();
        this.email = u.getEmail();
        this.senha = u.getSenha();
        this.celular = u.getCelular();
        this.imgPerfil = u.getImgPerfil();
        this.cep = enderecoIsNull(u.getEndereco()) ? null : u.getEndereco().getCep();
        this.logradouro = enderecoIsNull(u.getEndereco())  ? null : u.getEndereco().getLogradouro();
        this.numero = enderecoIsNull(u.getEndereco())  ? null : u.getEndereco().getNumero() ;
        this.complemento = enderecoIsNull(u.getEndereco())  ? null : u.getEndereco().getComplemento();
        this.cidade = enderecoIsNull(u.getEndereco())  ? null : u.getEndereco().getCidade();
        this.estado = enderecoIsNull(u.getEndereco()) ? null : u.getEndereco().getEstado();
        this.username = u.getUsername();
    }

    public static boolean enderecoIsNull(Endereco end){
        return end == null;
    }
}
