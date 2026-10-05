package planificador;

import java.util.ArrayList;
import java.util.List;

public class Resultado {
    private final String algoritmo;
    private final List<Proceso> procesos;                  // copias simuladas, en orden de fichero
    private final List<String> gantt = new ArrayList<>();  // una casilla por unidad ("-" = ociosa)
    private final List<Evento> traza = new ArrayList<>();
    private int cambiosContexto = 0;

    public Resultado(String algoritmo, List<Proceso> procesos) {
        this.algoritmo = algoritmo;
        this.procesos = procesos;
    }

    void anotarCasilla(String casilla) { gantt.add(casilla); }
    void anotarEvento(Evento e)        { traza.add(e); }
    void sumarCambioContexto()         { cambiosContexto++; }

    public String getAlgoritmo()       { return algoritmo; }
    public List<Proceso> getProcesos() { return procesos; }
    public List<String> getGantt()     { return gantt; }
    public List<Evento> getTraza()     { return traza; }
    public int getCambiosContexto()    { return cambiosContexto; }
}