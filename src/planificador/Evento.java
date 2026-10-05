package planificador;

public record Evento(int instante, String proceso,
                     EstadoProceso origen, EstadoProceso destino, String motivo) { }