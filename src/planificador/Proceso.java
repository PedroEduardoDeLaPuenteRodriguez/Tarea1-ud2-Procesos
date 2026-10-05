package planificador;

public class Proceso {
    private final String nombre;
    private final int llegada;
    private final int rafaga;
    private final int orden;      // posición en el fichero (desempates)

    private int restante;
    private EstadoProceso estado;
    private int inicio = -1;      // primer instante en CPU (-1 = aún no)
    private int fin = -1;         // instante de finalización

    public Proceso(String nombre, int llegada, int rafaga, int orden) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.orden = orden;
        this.restante = rafaga;
        this.estado = EstadoProceso.NUEVO;
    }

    /** Copia limpia (estado NUEVO, restante = ráfaga) para simular sin alterar el original. */
    public Proceso copia() {
        return new Proceso(nombre, llegada, rafaga, orden);
    }

    /** Cambia de estado solo si la transición es posible en el modelo. */
    public void cambiarEstado(EstadoProceso nuevo) {
        boolean valida = switch (estado) {
            case NUEVO -> nuevo == EstadoProceso.LISTO;
            case LISTO -> nuevo == EstadoProceso.EJECUCION;
            case EJECUCION -> nuevo == EstadoProceso.LISTO
                    || nuevo == EstadoProceso.BLOQUEADO
                    || nuevo == EstadoProceso.TERMINADO;
            case BLOQUEADO -> nuevo == EstadoProceso.LISTO;
            case TERMINADO -> false;
        };
        if (!valida) {
            throw new IllegalStateException(
                    "Transición imposible en " + nombre + ": " + estado + " -> " + nuevo);
        }
        this.estado = nuevo;
    }

    /** Ejecuta una unidad de tiempo en el instante t. */
    public void ejecutarUnidad(int t) {
        if (inicio < 0) inicio = t;
        restante--;
    }

    public void terminar(int instanteFin) {
        this.fin = instanteFin;
    }

    // Métricas (definidas en el enunciado)
    public int getRetorno()   { return fin - llegada; }
    public int getEspera()    { return getRetorno() - rafaga; }
    public int getRespuesta() { return inicio - llegada; }

    public String getNombre()  { return nombre; }
    public int getLlegada()    { return llegada; }
    public int getRafaga()     { return rafaga; }
    public int getOrden()      { return orden; }
    public int getRestante()   { return restante; }
    public int getInicio()     { return inicio; }
    public int getFin()        { return fin; }
    public EstadoProceso getEstado() { return estado; }
}