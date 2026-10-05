package planificador;

import java.util.List;

public class RoundRobin extends Planificador {

    private final int quantum;

    public RoundRobin(int quantum) {
        if (quantum <= 0) {
            throw new IllegalArgumentException("El quantum debe ser mayor que 0 (recibido: " + quantum + ")");
        }
        this.quantum = quantum;
    }

    @Override
    public String getNombre() {
        return "Round Robin (q=" + quantum + ")";
    }

    @Override
    protected Proceso elegir(List<Proceso> listos) {
        return listos.get(0);
    }

    @Override
    protected boolean agotaTurno(int usadoEnTurno) {
        return usadoEnTurno >= quantum;
    }
}