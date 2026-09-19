package controller;

import java.util.ArrayList;
import java.util.List;

import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;

public class GestorPedidos {
    private final List<Pedido> pedidos = new ArrayList<>();

    public void agregarPedido(Pedido pedido) {
        if (buscarPedido(pedido.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un pedido con ese ID.");
        }

        pedidos.add(pedido);
    }

    public List<Pedido> obtenerPedidos() {
        return new ArrayList<>(pedidos);
    }

    public List<Pedido> obtenerPedidosPendientes() {
        List<Pedido> pendientes = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {
                pendientes.add(pedido);
            }
        }

        return pendientes;
    }

    public void iniciarEntrega(Pedido pedido, Repartidor repartidor) {
        pedido.iniciarEntrega(repartidor);
    }

    private Pedido buscarPedido(int id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }

        return null;
    }
}
