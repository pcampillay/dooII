package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Pedido {
    private String idPedido;
    private String direccionEntrega;
    private String tipoPedido;
    private int distanciaKm;
    private String repartidorAsignado;
    private boolean reservado;
    private boolean cancelado;
    private boolean despachado;
    private final List<String> historial;

    public Pedido(String idPedido, String direccionEntrega, String tipoPedido, int distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        this.distanciaKm = distanciaKm;
        historial = new ArrayList<>();
        registrarEvento("Pedido creado.");
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

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public boolean estaReservado() {
        return reservado;
    }

    public boolean estaCancelado() {
        return cancelado;
    }

    public boolean estaDespachado() {
        return despachado;
    }

    public List<String> getHistorial() {
        return new ArrayList<>(historial);
    }

    public void mostrarResumen() {
        System.out.println("[Pedido " + tipoPedido + "]");
        System.out.println("Pedido #" + idPedido);
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
        if (repartidorAsignado != null) {
            System.out.println("Repartidor asignado: " + repartidorAsignado);
        }
    }

    public abstract int calcularTiempoEntrega();

    public void asignarRepartidor() {
        registrarRepartidor("Repartidor disponible");
        System.out.println("Repartidor asignado automáticamente.");
    }

    public void asignarRepartidor(String nombreRepartidor) {
        registrarRepartidor(nombreRepartidor);
        System.out.println("Pedido asignado manualmente a " + nombreRepartidor + ".");
    }

    public void reservar() {
        if (cancelado || despachado) {
            System.out.println("No se puede reservar el pedido #" + idPedido + ".");
            return;
        }

        if (reservado) {
            System.out.println("El pedido #" + idPedido + " ya estaba reservado.");
            return;
        }

        reservado = true;
        registrarEvento("Pedido reservado.");
        System.out.println("Pedido #" + idPedido + " reservado correctamente.");
    }

    public void verHistorial() {
        System.out.println("Historial del Pedido " + tipoPedido + " #" + idPedido + ":");
        for (String evento : historial) {
            System.out.println("- " + evento);
        }
        System.out.println();
    }

    protected void registrarRepartidor(String nombreRepartidor) {
        repartidorAsignado = nombreRepartidor;
        registrarEvento("Repartidor asignado: " + nombreRepartidor + ".");
    }

    protected void despacharPedido(String mensaje) {
        if (cancelado) {
            System.out.println("No se puede despachar el pedido #" + idPedido + " porque está cancelado.");
            return;
        }

        if (!reservado || repartidorAsignado == null) {
            System.out.println("El pedido #" + idPedido + " debe estar reservado y tener repartidor.");
            return;
        }

        if (despachado) {
            System.out.println("El pedido #" + idPedido + " ya fue despachado.");
            return;
        }

        despachado = true;
        registrarEvento(mensaje);
        System.out.println(mensaje);
    }

    protected void cancelarPedido(String mensaje) {
        if (despachado) {
            System.out.println("No se puede cancelar el pedido #" + idPedido + " porque ya fue despachado.");
            return;
        }

        if (cancelado) {
            System.out.println("El pedido #" + idPedido + " ya estaba cancelado.");
            return;
        }

        cancelado = true;
        registrarEvento(mensaje);
        System.out.println(mensaje);
    }

    private void registrarEvento(String evento) {
        historial.add(evento);
    }
}
