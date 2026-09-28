package model;

public class Pedido {
    private Integer id;
    private final String direccion;
    private final TipoPedido tipo;
    private EstadoPedido estado;

    public Pedido(String direccion, TipoPedido tipo) {
        this(null, direccion, tipo, EstadoPedido.PENDIENTE);
    }

    public Pedido(Integer id, String direccion, TipoPedido tipo, EstadoPedido estado) {
        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " - " + direccion;
    }
}
