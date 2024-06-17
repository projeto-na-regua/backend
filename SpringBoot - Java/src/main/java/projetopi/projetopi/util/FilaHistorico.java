package projetopi.projetopi.util;

import projetopi.projetopi.dto.response.AgendamentoConsulta;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class FilaHistorico {

    private Queue<AgendamentoConsulta> fila;

    public FilaHistorico() {
        this.fila = new LinkedList<>();
    }

    public void adicionar(AgendamentoConsulta agendamento) {
        fila.offer(agendamento);
    }

    public AgendamentoConsulta remover() {
        return fila.poll();
    }

    public boolean isEmpty() {
        return fila.isEmpty();
    }

    public int size() {
        return fila.size();
    }

    public Queue<AgendamentoConsulta> getFila() {
        return fila;
    }

    public List<AgendamentoConsulta> getHistorico() {
        return new LinkedList<>(fila);
    }
}
