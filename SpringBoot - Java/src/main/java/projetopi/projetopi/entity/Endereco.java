package projetopi.projetopi.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnTransformer;

import org.hibernate.annotations.Type;
import org.hibernate.spatial.dialect.sqlserver.SqlServerGeographyType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import projetopi.projetopi.dto.response.BarbeariaConsulta;



@Entity
@Getter
@Setter
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco", nullable = false)
    private Integer id;

    @Size(max = 9)
    @Column(name = "cep")
    private String cep;
    @Column(name = "logradouro")
    private String logradouro;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "cidade")
    private String cidade;
    @Column(name = "estado")
    private String estado;
    @Column(name = "localizacao", nullable = false, columnDefinition = "POINT")
    private Point localizacao;

    public Endereco() {
    }

    public Endereco(String cep, String logradouro, Integer numero, String complemento, String cidade, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Endereco(BarbeariaConsulta barbeariaConsulta) {
        this.cep = barbeariaConsulta.getCep();
        this.logradouro = barbeariaConsulta.getLogradouro();
        this.numero = barbeariaConsulta.getNumero();
        this.complemento = barbeariaConsulta.getComplemento();
        this.cidade = barbeariaConsulta.getCidade();
        this.estado = barbeariaConsulta.getEstado();
    }
}
