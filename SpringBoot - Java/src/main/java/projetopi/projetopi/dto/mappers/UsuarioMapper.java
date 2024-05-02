package projetopi.projetopi.dto.mappers;

import projetopi.projetopi.dominio.*;
import projetopi.projetopi.dto.request.CadastroBarbearia;
import projetopi.projetopi.dto.request.CadastroCliente;

public class UsuarioMapper {

    public static Cliente toEntity(CadastroCliente c){
        return new Cliente(c.getNome(), c.getEmail(), c.getCelular(),
        new Endereco(c.getCep(),c.getLogradouro(), c.getNumero(),c.getComplemento(), c.getCidade(), c.getEstado()));

    }

    public static Barbeiro toEntity(CadastroBarbearia c){
        return new Barbeiro(c.getNomeUsuario(), c.getEmail(), c.getCelularUsuario(), true,
                new Barbearia(c.getNomeDoNegocio()));

    }


}
