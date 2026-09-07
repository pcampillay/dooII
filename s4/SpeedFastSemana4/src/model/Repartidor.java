package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import operaciones.Despachable;

public class Repartidor implements Runnable {
    private final String nombre;
    private final List<Pedido> pedidosAsignados;

    public Repartidor(String nombre, List<Pedido> pedidosAsignados) {
        this.nombre = nombre;
        this.pedidosAsignados = new ArrayList<>(pedidosAsignados);
    }

    @Override
    public void run() {
        for (Pedido pedido : pedidosAsignados) {
            entregarPedido(pedido);
        }
    }

    private void entregarPedido(Pedido pedido) {
        System.out.println("[Repartidor: " + nombre + "] Entregando "
                + pedido.getClass().getSimpleName() + " #" + pedido.getIdPedido() + "...");

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1001));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("[Repartidor: " + nombre + "] Entrega interrumpida.");
            return;
        }

        if (pedido instanceof Despachable) {
            ((Despachable) pedido).despachar();
        }

        System.out.println("[Repartidor: " + nombre + "] Pedido #"
                + pedido.getIdPedido() + " entregado.");
    }
}
