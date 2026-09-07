package model;

import operaciones.Cancelable;
import operaciones.Despachable;
import operaciones.Rastreable;

public class PedidoEncomienda extends Pedido implements Despachable, Cancelable, Rastreable {
    private static final int TIEMPO_BASE = 20;
    private static final double MINUTOS_POR_KM = 1.5;

    public PedidoEncomienda(String idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, "Encomienda", distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(TIEMPO_BASE + MINUTOS_POR_KM * getDistanciaKm());
    }

    @Override
    public void asignarRepartidor() {
        registrarRepartidor("Repartidor de carga disponible");
        System.out.println("Repartidor de carga asignado automáticamente.");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        registrarRepartidor(nombreRepartidor);
        System.out.println("Encomienda asignada manualmente a " + nombreRepartidor + ".");
    }

    @Override
    public void despachar() {
        despacharPedido("Encomienda despachada con embalaje validado.");
    }

    @Override
    public void cancelar() {
        cancelarPedido("Encomienda cancelada; se avisó al área de carga.");
    }
}
