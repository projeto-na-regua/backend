package projetopi.projetopi.dto.response;

import projetopi.projetopi.dominio.Financa;

import java.time.LocalDateTime;

public class InfoFinanca {

    private Double valor;

    private LocalDateTime dtLancamento;

    private Integer barbeariaId;

    public InfoFinanca() {}

    public InfoFinanca(Financa financa) {
        this.barbeariaId = financa.getBarbeariaId();
        this.dtLancamento = financa.getDtLancamento();
        this.valor = financa.getValor();
    }

    public Integer getBarbeariaId() {
        return barbeariaId;
    }

    public LocalDateTime getDtLancamento() {
        return dtLancamento;
    }

    public Double getValor() {
        return valor;
    }
}
