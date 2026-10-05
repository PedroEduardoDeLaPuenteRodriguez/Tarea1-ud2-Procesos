package planificador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * PSP · Tema 2 · Simulador de planificación para NexoData
 *
 * Punto de entrada. Este fichero YA ESTÁ HECHO: solo lee y comprueba los argumentos.
 * No cambies cómo se leen: el profesor ejecutará tu programa siempre así:
 *
 *   java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]
 *
 * Todo lo demás (modelo del proceso, lectura del CSV, algoritmos, métricas,
 * informe por consola...) lo diseñas y programas tú en este mismo paquete.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]");
            System.exit(1);
        }
        Path fichero = Path.of(args[0]);
        String algoritmo = args[1].toLowerCase();
        boolean traza = List.of(args).contains("--traza");
        int quantum = 2;
        if (args.length >= 3 && !args[2].startsWith("--")) {
            try {
                quantum = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("El quantum debe ser un número entero: " + args[2]);
                System.exit(1);
            }
        }
        if (!Files.exists(fichero)) {
            System.err.println("No encuentro el fichero " + fichero.toAbsolutePath()
                    + "\nComprueba el «Working directory» de la configuración de ejecución.");
            System.exit(1);
        }
        if (!List.of("fcfs", "sjf", "rr", "todos").contains(algoritmo)) {
            System.err.println("Algoritmo desconocido: " + algoritmo + " (usa fcfs, sjf, rr o todos)");
            System.exit(1);
        }

        System.out.println("Fichero: " + fichero + " | algoritmo: " + algoritmo
                + " | quantum: " + quantum + " | traza: " + traza);

        // TODO (tareas 1 a 3): a partir de aquí, lee los procesos del fichero,
        // simula el algoritmo o algoritmos pedidos y muestra los resultados.
        // Cuando lo tengas, borra el println de arriba y este comentario.

        // --- Tarea 2: comprobación temporal de los algoritmos (se sustituirá en la tarea 3) ---
        try {
            List<Proceso> procesos = LectorProcesos.leer(fichero.toString());

            List<Planificador> algoritmos = new java.util.ArrayList<>();
            if (algoritmo.equals("fcfs") || algoritmo.equals("todos")) algoritmos.add(new FCFS());
            if (algoritmo.equals("sjf")  || algoritmo.equals("todos")) algoritmos.add(new SJF());
            if (algoritmo.equals("rr")   || algoritmo.equals("todos")) algoritmos.add(new RoundRobin(quantum));

            for (Planificador pl : algoritmos) {
                Resultado r = pl.simular(procesos);
                System.out.println("=== " + r.getAlgoritmo() + " ===");
                System.out.println("CPU: " + String.join(" ", r.getGantt()));
                for (Proceso p : r.getProcesos()) {
                    System.out.println(p.getNombre() + " fin=" + p.getFin());
                }
                System.out.println("Cambios de contexto: " + r.getCambiosContexto());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (java.io.IOException e) {
            System.err.println("Error leyendo el fichero: " + e.getMessage());
            System.exit(1);
        }
    }
}
