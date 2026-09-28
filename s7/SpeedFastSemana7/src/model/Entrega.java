package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entrega {
    private final Pedido pedido;
    private final Repartidor repartidor;
    private final LocalDate fecha;
    private final LocalTime hora;

    public Entrega(Pedido pedido, Repartidor repartidor, LocalDate fecha, LocalTime hora) {
        this.pedido = pedido;
        this.repartidor = repartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}
