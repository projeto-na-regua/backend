package projetopi.projetopi.util;

import projetopi.projetopi.dominio.*;

import java.util.Arrays;

public class OrdenadorDeAgendaPorTempoEstimado {

    public static AgendaAux[] ordenarPorTempoEstimado(AgendaAux[] vetorAgenda){
        for (int i = 0; i < vetorAgenda.length - 1; i++) {
            int indMenor = i;
            AgendaAux aux;
            for (int j = i + 1; j < vetorAgenda.length; j++) {
                if (vetorAgenda[j].getTempoEstimado() < vetorAgenda[indMenor].getTempoEstimado()) {
                    indMenor = j;
                }
            }

            aux = vetorAgenda[indMenor];
            vetorAgenda[indMenor] = vetorAgenda[i];
            vetorAgenda[i] = aux;
        }

            return vetorAgenda;
    }

    public static void main(String[] args) {

        AgendaAux[] vetorAgendamento = new AgendaAux[5];

        // Criando uma barbearia
        Barbearia barbearia = new Barbearia("Barbearia do João", "999999999", "barbearia@joao.com", "12345-678", "Rua das Flores", "100", "São Paulo");

        // Criando um barbeiro
        Barbeiro barbeiro = new Barbeiro("João", "888888888", "joao@barbeiro.com", true);

        // Criando um cliente
        Cliente cliente = new Cliente("Maria", "777777777", "maria@gmail.com");

        // Criando um serviço
        //Servico servico = new Servico("Corte de Cabelo", 50.0, 30);

        Barbearia barbearia2 = new Barbearia("Barbearia da Maria", "888888888", "barbearia@maria.com", "54321-876", "Avenida Principal", "200", "Rio de Janeiro");

        // Criando um barbeiro
        Barbeiro barbeiro2 = new Barbeiro("Pedro", "999999999", "pedro@barbeiro.com", false);

        // Criando um cliente
        Cliente cliente2 = new Cliente("Ana", "666666666", "ana@gmail.com");

        // Criando um serviço
        //Servico servico2 = new Servico("Barba", 30.0, 20);

        Barbearia barbearia3 = new Barbearia("Barbearia do Carlos", "111111111", "barbearia@carlos.com", "12345-678", "Rua A", "10", "São Paulo");
        Barbearia barbearia4 = new Barbearia("Barbearia da Paula", "222222222", "barbearia@paula.com", "98765-432", "Avenida B", "20", "Rio de Janeiro");
        Barbearia barbearia5 = new Barbearia("Barbearia do Marcos", "333333333", "barbearia@marcos.com", "54321-098", "Praça C", "30", "Curitiba");

        // Criando barbeiros
        Barbeiro barbeiro3 = new Barbeiro("Pedro", "999999999", "pedro@barbeiro.com", false);
        Barbeiro barbeiro4 = new Barbeiro("Lucas", "777777777", "lucas@barbeiro.com", false);
        Barbeiro barbeiro5 = new Barbeiro("Marcos", "555555555", "marcos@barbeiro.com", true);

        // Criando clientes
        Cliente cliente3 = new Cliente("Ana", "666666666", "ana@gmail.com");
        Cliente cliente4 = new Cliente("João", "444444444", "joao@gmail.com");
        Cliente cliente5 = new Cliente("Carla", "222222222", "carla@gmail.com");

        // Criando serviços
        //Servico servico3 = new Servico("Barba", 30.0, 20);
        //Servico servico4 = new Servico("Corte de Cabelo", 50.0, 30);
        //Servico servico5 = new Servico("Pintura de Cabelo", 80.0, 60);

        // Agendando o serviço
        //vetorAgendamento[0] =  cliente.agendar(barbearia, barbeiro, cliente, servico);
        //vetorAgendamento[1] = cliente2.agendar(barbearia2, barbeiro2, cliente2, servico2);
        //vetorAgendamento[2] = cliente3.agendar(barbearia3, barbeiro3, cliente3, servico3);
        //vetorAgendamento[3] = cliente4.agendar(barbearia4, barbeiro4, cliente4, servico4);
        //vetorAgendamento[4] = cliente5.agendar(barbearia5, barbeiro5, cliente5, servico5);

        System.out.println(Arrays.toString(ordenarPorTempoEstimado(vetorAgendamento)));
    }
}
