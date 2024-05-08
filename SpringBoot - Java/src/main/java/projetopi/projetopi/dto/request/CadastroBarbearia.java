package projetopi.projetopi.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import projetopi.projetopi.dominio.Barbearia;
import projetopi.projetopi.dominio.Barbeiro;
import projetopi.projetopi.dominio.DiaSemana;
import projetopi.projetopi.dominio.Endereco;
import projetopi.projetopi.util.Dia;

import java.security.cert.TrustAnchor;
@Getter
public class CadastroBarbearia {


    private String nomeDoNegocio;

    @CPF
    private String cpf;

    @Size(max = 9)
    private String cep;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String cidade;

   private String estado;

   public DiaSemana[] gerarSemena(){
        DiaSemana[] semana = new DiaSemana[7];

        semana[0] = new DiaSemana(Dia.SEG);
        semana[1] = new DiaSemana(Dia.TER);
        semana[2] = new DiaSemana(Dia.QUA);
        semana[3] = new DiaSemana(Dia.QUI);
        semana[4] = new DiaSemana(Dia.SEX);
        semana[5] = new DiaSemana(Dia.SAB);
        semana[6] = new DiaSemana(Dia.DOM);

        return semana;

    }

   public Barbearia gerarBarbearia(){

       Barbearia barbearia = new Barbearia();

       barbearia.setNomeNegocio(nomeDoNegocio);
       barbearia.setCpf(cpf);

       return barbearia;
   }

   public Endereco gerarEndereco(){

       Endereco endereco = new Endereco();

       endereco.setCep(cep);
       endereco.setLogradouro(logradouro);
       endereco.setNumero(numero);
       endereco.setCidade(cidade);
       endereco.setEstado(estado);

       if (!complemento.isEmpty()){
           endereco.setComplemento(complemento);
       }

       return endereco;
   }

}
