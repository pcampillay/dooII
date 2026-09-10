package model;

import java.util.ArrayList;
import java.util.List;

public class ZonaDeCarga {
    private final List<Pedido> pedidosPendientes = new ArrayList<>();

    public synchronized void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedidosPendientes.add(pedido);
        System.out.println(pedido + " agregado.");
    }

    public synchronized Pedido retirarPedido() {
        if (pedidosPendientes.isEmpty()) {
            return null;
        }

        return pedidosPendientes.remove(0);
    }

    public synchronized boolean estaVacia() {
        return pedidosPendientes.isEmpty();
    }
}
