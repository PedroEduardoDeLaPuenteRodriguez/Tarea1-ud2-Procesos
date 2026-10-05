package planificador;

import java.util.ArrayList;
import java.util.List;

public abstract class Planificador {

    public abstract String getNombre();

    /** Decide a quién poner en la CPU entre los procesos listos (la lista no está vacía). */
    protected abstract Proceso elegir(List<Proceso> listos);

    /** Decide si el proceso en CPU debe dejarla tras usar 'usadoEnTurno' unidades seguidas. */
    protected boolean agotaTurno(int usadoEnTurno) {
        return false; // por defecto, sin desalojo
    }

    public Resultado simular(List<Proceso> originales) {
        // Copias limpias: los originales no se tocan y se pueden simular varios algoritmos seguidos.
        List<Proceso> procesos = new ArrayList<>();
        for (Proceso p : originales) {
            procesos.add(p.copia());
        }

        Resultado res = new Resultado(getNombre(), procesos);
        List<Proceso> listos = new ArrayList<>();
        Proceso enCpu = null;
        Proceso ultimo = null;   // último proceso que ejecutó (para los cambios de contexto)
        int usadoEnTurno = 0;
        int terminados = 0;
        int t = 0;

        while (terminados < procesos.size()) {

            // 1) Llegadas en t (en orden de fichero: regla 2)
            for (Proceso p : procesos) {
                if (p.getLlegada() == t) {
                    mover(res, t, p, EstadoProceso.LISTO, "llega al sistema");
                    listos.add(p);
                }
            }

            // 2) ¿Ha terminado o agotado el turno el proceso en CPU?
            if (enCpu != null) {
                if (enCpu.getRestante() == 0) {
                    enCpu.terminar(t);
                    mover(res, t, enCpu, EstadoProceso.TERMINADO, "termina su ráfaga");
                    terminados++;
                    enCpu = null;
                } else if (agotaTurno(usadoEnTurno)) {
                    if (listos.isEmpty()) {
                        usadoEnTurno = 0;   // regla 4: nadie espera, sigue con quantum nuevo
                    } else {
                        mover(res, t, enCpu, EstadoProceso.LISTO, "agota el quantum");
                        listos.add(enCpu);  // entra DESPUÉS de las llegadas de t (regla 3)
                        enCpu = null;
                    }
                }
            }

            // 3) Si la CPU está libre, elige el algoritmo
            if (enCpu == null && !listos.isEmpty()) {
                enCpu = elegir(listos);
                listos.remove(enCpu);
                mover(res, t, enCpu, EstadoProceso.EJECUCION, "el planificador lo elige");
                if (ultimo != null && ultimo != enCpu) {
                    res.sumarCambioContexto();
                }
                ultimo = enCpu;
                usadoEnTurno = 0;
            }

            if (terminados == procesos.size()) {
                break;   // no añadir una casilla de más al final
            }

            // 4) Ejecutar una unidad (o CPU ociosa) y avanzar
            if (enCpu != null) {
                res.anotarCasilla(enCpu.getNombre());
                enCpu.ejecutarUnidad(t);
                usadoEnTurno++;
            } else {
                res.anotarCasilla("-");
            }
            t++;
        }
        return res;
    }

    /** Cambia el estado (validando la transición) y la registra en la traza. */
    private void mover(Resultado res, int t, Proceso p, EstadoProceso destino, String motivo) {
        EstadoProceso origen = p.getEstado();
        p.cambiarEstado(destino);
        res.anotarEvento(new Evento(t, p.getNombre(), origen, destino, motivo));
    }
}