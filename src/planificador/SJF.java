package planificador;

import java.util.List;

public class SJF extends Planificador {

    @Override
    public String getNombre() {
        return "SJF";
    }

    @Override
    protected Proceso elegir(List<Proceso> listos) {
        Proceso mejor = listos.get(0);
        for (Proceso p : listos) {
            if (esMejor(p, mejor)) {
                mejor = p;
            }
        }
        return mejor;
    }

    private boolean esMejor(Proceso a, Proceso b) {
        if (a.getRafaga() != b.getRafaga()) return a.getRafaga() < b.getRafaga();
        if (a.getLlegada() != b.getLlegada()) return a.getLlegada() < b.getLlegada();
        return a.getOrden() < b.getOrden();
    }
}