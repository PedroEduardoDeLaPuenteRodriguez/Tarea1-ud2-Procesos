package planificador;

import java.util.List;
import java.util.Locale;

public class Informe {

    private static final Locale ES = Locale.forLanguageTag("es-ES"); // decimales con coma, como la referencia
    private static final int CASILLAS_POR_FILA = 20;                  // el Gantt largo se parte en filas

    public static void imprimir(Resultado r, boolean traza) {
        System.out.println("=== " + r.getAlgoritmo() + " ===");
        imprimirGantt(r.getGantt());
        System.out.println();
        imprimirTabla(r.getProcesos());
        System.out.println("Cambios de contexto: " + r.getCambiosContexto());
        if (traza) {
            imprimirTraza(r.getTraza());
        }
        System.out.println();
    }

    private static void imprimirGantt(List<String> gantt) {
        int ancho = String.valueOf(gantt.size()).length();
        for (String casilla : gantt) {
            ancho = Math.max(ancho, casilla.length());
        }
        ancho++; // un espacio de separación

        for (int ini = 0; ini < gantt.size(); ini += CASILLAS_POR_FILA) {
            int fin = Math.min(ini + CASILLAS_POR_FILA, gantt.size());
            StringBuilder filaTiempo = new StringBuilder(String.format("%-4s", "t"));
            StringBuilder filaCpu = new StringBuilder(String.format("%-4s", "CPU"));
            for (int i = ini; i < fin; i++) {
                filaTiempo.append(String.format("%-" + ancho + "d", i));
                filaCpu.append(String.format("%-" + ancho + "s", gantt.get(i)));
            }
            System.out.println(filaTiempo.toString().stripTrailing());
            System.out.println(filaCpu.toString().stripTrailing());
        }
    }

    private static void imprimirTabla(List<Proceso> procesos) {
        System.out.printf("%-10s %7s %7s %5s %8s %7s %9s%n",
                "Proceso", "Llegada", "Ráfaga", "Fin", "Retorno", "Espera", "Respuesta");
        double sumaRetorno = 0, sumaEspera = 0, sumaRespuesta = 0;
        for (Proceso p : procesos) {
            System.out.printf("%-10s %7d %7d %5d %8d %7d %9d%n",
                    p.getNombre(), p.getLlegada(), p.getRafaga(), p.getFin(),
                    p.getRetorno(), p.getEspera(), p.getRespuesta());
            sumaRetorno += p.getRetorno();
            sumaEspera += p.getEspera();
            sumaRespuesta += p.getRespuesta();
        }
        int n = procesos.size();
        System.out.println(String.format(ES,
                "Medias: retorno %.2f | espera %.2f | respuesta %.2f",
                sumaRetorno / n, sumaEspera / n, sumaRespuesta / n));
    }

    private static void imprimirTraza(List<Evento> traza) {
        System.out.println();
        System.out.println("Traza de estados:");
        for (Evento e : traza) {
            System.out.printf("  t=%-3d %-10s %-10s -> %-10s (%s)%n",
                    e.instante(), e.proceso(), e.origen(), e.destino(), e.motivo());
        }
    }
}