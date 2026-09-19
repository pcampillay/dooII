package model;

public class Pedido {
    private final int id;
    private final String direccionEntrega;
    private final TipoPedido tipo;
    private EstadoPedido estado;
    private Repartidor repartidor;

    public Pedido(int id, String direccionEntrega, TipoPedido tipo) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.tipo = tipo;
        estado = EstadoPedido.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void iniciarEntrega(Repartidor nuevoRepartidor) {
        repartidor = nuevoRepartidor;
        estado = EstadoPedido.EN_REPARTO;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " - " + direccionEntrega;
    }
}
