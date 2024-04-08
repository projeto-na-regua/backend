package projetopi.projetopi.util;

public enum Dia {

    SEG("Segunda"),
    TER("Terça"),
    QUAR("Quarta"),

    QUI("Quinta"),

    SEX("Sexta"),
        
    SAB("Sábado"),

    DOM("Domingo");

    private final String nome;


    Dia(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
