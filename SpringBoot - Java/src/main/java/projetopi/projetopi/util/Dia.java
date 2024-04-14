package projetopi.projetopi.util;

import java.util.concurrent.ThreadLocalRandom;

import java.util.concurrent.ThreadLocalRandom;

public enum Dia {
    SEG("SEG"),
    TER("TER"),
    QUA("QUA"),
    QUI("QUI"),
    SEX("SEX"),
    SAB("SAB"),
    DOM("DOM");

    private final String nome;

    Dia(String nome) {
        this.nome = nome;
    }


    public String getNome() {
        return nome;
    }
}

