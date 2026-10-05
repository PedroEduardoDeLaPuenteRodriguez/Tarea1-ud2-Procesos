package planificador;

import java.util.List;

public class FCFS extends Planificador {

    @Override
    public String getNombre() {
        return "FCFS";
    }

    @Override
    protected Proceso elegir(List<Proceso> listos) {
        return listos.get(0);
    }
}