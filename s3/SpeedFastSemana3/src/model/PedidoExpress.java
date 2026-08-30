package model;

import operaciones.Cancelable;
import operaciones.Despachable;
import operaciones.Rastreable;

public class PedidoExpress extends Pedido implements Despachable, Cancelable, Rastreable {
    private static final int TIEMPO_BASE = 10;
    private static final int DISTANCIA_LIMITE = 5;
    private static final int TIEMPO_EXTRA = 5;

    public PedidoExpress(String idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, "Express", distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        if (getDistanciaKm() > DISTANCIA_LIMITE) {
            return TIEMPO_BASE + TIEMPO_EXTRA;
        }

        return TIEMPO_BASE;
    }

    @Override
    public void asignarRepartidor() {
        registrarRepartidor("Repartidor express cercano");
        System.out.println("Repartidor más cercano asignado automáticamente.");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        registrarRepartidor(nombreRepartidor);
        System.out.println("Pedido express asignado manualmente a " + nombreRepartidor + ".");
    }

    @Override
    public void despachar() {
        despacharPedido("Pedido express despachado con prioridad inmediata.");
    }

    @Override
    public void cancelar() {
        cancelarPedido("Pedido express cancelado; se avisó al repartidor más cercano.");
    }
}
