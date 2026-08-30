package app;

import java.util.ArrayList;
import java.util.List;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import operaciones.Cancelable;
import operaciones.Despachable;
import operaciones.Rastreable;

public class Main {

    public static void main(String[] args) {
        PedidoComida pedidoComida = new PedidoComida("101", "Av. Italia 456", 4);
        PedidoEncomienda pedidoEncomienda = new PedidoEncomienda("102", "Av. Santa Rosa 567", 7);
        PedidoExpress pedidoExpress = new PedidoExpress("103", "Av. Apoquindo 1500", 6);

        // Caso 1: asignación automática y despacho de comida.
        mostrarTitulo("CASO 1: PEDIDO DE COMIDA");
        pedidoComida.asignarRepartidor();
        pedidoComida.reservar();
        mostrarTiempoEstimado(pedidoComida);
        despacharPedido(pedidoComida);

        // Caso 2: asignación manual y despacho de encomienda.
        mostrarTitulo("CASO 2: PEDIDO DE ENCOMIENDA");
        pedidoEncomienda.asignarRepartidor("Daniela Tapia");
        pedidoEncomienda.reservar();
        mostrarTiempoEstimado(pedidoEncomienda);
        despacharPedido(pedidoEncomienda);

        // Caso 3: reserva y cancelación de pedido express.
        mostrarTitulo("CASO 3: PEDIDO EXPRESS");
        pedidoExpress.asignarRepartidor();
        pedidoExpress.reservar();
        mostrarTiempoEstimado(pedidoExpress);
        cancelarPedido(pedidoExpress);

        mostrarTitulo("HISTORIAL DE PEDIDOS");
        List<Rastreable> pedidosRastreables = new ArrayList<>();
        pedidosRastreables.add(pedidoComida);
        pedidosRastreables.add(pedidoEncomienda);
        pedidosRastreables.add(pedidoExpress);

        for (Rastreable pedido : pedidosRastreables) {
            pedido.verHistorial();
        }
    }

    private static void mostrarTiempoEstimado(Pedido pedido) {
        pedido.mostrarResumen();
        System.out.println("Tiempo estimado: "
                + pedido.calcularTiempoEntrega() + " minutos");
    }

    private static void despacharPedido(Despachable pedido) {
        pedido.despachar();
    }

    private static void cancelarPedido(Cancelable pedido) {
        pedido.cancelar();
    }

    private static void mostrarTitulo(String titulo) {
        System.out.println("\n=== " + titulo + " ===");
    }
}
