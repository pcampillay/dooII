package model;

import operaciones.Cancelable;
import operaciones.Despachable;
import operaciones.Rastreable;

public class PedidoComida extends Pedido implements Despachable, Cancelable, Rastreable {
    private static final int TIEMPO_BASE = 15;
    private static final int MINUTOS_POR_KM = 2;

    public PedidoComida(String idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, "Comida", distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        return TIEMPO_BASE + MINUTOS_POR_KM * getDistanciaKm();
    }

    @Override
    public void asignarRepartidor() {
        registrarRepartidor("Luis Díaz");
        System.out.println("Repartidor con mochila térmica asignado automáticamente.");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        registrarRepartidor(nombreRepartidor);
        System.out.println("Pedido de comida asignado manualmente a " + nombreRepartidor + ".");
    }

    @Override
    public void despachar() {
        despacharPedido("Pedido de comida despachado en mochila térmica.");
    }

    @Override
    public void cancelar() {
        cancelarPedido("Pedido de comida cancelado; se avisó al local.");
    }
}
