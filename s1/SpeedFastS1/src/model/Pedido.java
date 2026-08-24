package model;

public abstract class Pedido {
    private String idPedido;
    private String direccionEntrega;
    private String tipoPedido;
    private int distanciaKm;

    public Pedido(String idPedido, String direccionEntrega, String tipoPedido, int distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        this.distanciaKm = distanciaKm;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public int getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(int distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public void mostrarResumen() {
        System.out.println("Pedido" + tipoPedido + " #" + idPedido);
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
    }

    public abstract int calcularTiempoEntrega();

    public void asignarRepartidor() {
        System.out.println("[Pedido " + tipoPedido + "]");
        System.out.println("Asignando un repartidor disponible...");
    }

    public void asignarRepartidor(String nombreRepartidor) {
        asignarRepartidor();
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}
