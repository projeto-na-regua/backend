package projetopi.projetopi.relatorios;

import projetopi.projetopi.entity.Barbeiro;

import java.io.*;
import java.util.*;

public class RelatorioBarbeiro {
    //trocar a tipagem da lista para o tipo da sua classe
    public static void gravarRelatorioFinanceiro(List<Barbeiro> lista, ByteArrayOutputStream outputStream) {
        Formatter saida = new Formatter(outputStream);

        //Cabeçalho:
        saida.format("%s;%s;%s;%s\n", "NOME_BARBEIRO","CELULAR","E-MAIL","ADMINISTRADOR");

        try {
            for (int i = 0; i < lista.size(); i++) {
                //Recupere um elemento da lista e formate aqui:
                Barbeiro b = lista.get(i);
                saida.format("%s;%s;%s;%s\n", b.getNome(), b.getCelular(), b.getEmail(), b.isAdm() ? "Sim" : "Não");
            }
        } catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
        } finally {
            saida.close();
        }
    }

    public static void leArquivoCsv(String nomeArq) {
        FileReader arq = null;
        Scanner entrada = null;
        Boolean deuRuim = false;

        nomeArq += ".csv";

        // Bloco try-catch para abrir o arquivo
        try {
            arq = new FileReader(nomeArq);
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        } catch (FileNotFoundException erro) {
            System.out.println("Arquivo nao encontrado");
            System.exit(1);
        }

        // Bloco try-catch para ler o arquivo
        try {
        //Leia e formate a saída no console aqui:

            // Cabeçalho
            System.out.printf("%-4S %-15S %-7S %5S\n", "id", "nome", "porte", "peso");
            while (entrada.hasNext()) {
                //Corpo
                int id = entrada.nextInt();
                String nome = entrada.next();
                String porte = entrada.next();
                Double peso = entrada.nextDouble();

                System.out.printf("%04d %-15s %-7S %5.1f\n", id, nome, porte, peso);
            }
        } catch (NoSuchElementException erro) {
            System.out.println("Arquivo com problemas");
            deuRuim = true;
        } catch (IllegalStateException erro) {
            System.out.println("Erro na leitura do arquivo");
            deuRuim = true;
        } finally {
            entrada.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }
}
