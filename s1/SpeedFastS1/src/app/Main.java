package app;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

public class Main {

    public static void main(String[] args) {
        Pedido pedidoComida = new PedidoComida("001", "Av. Italia 456", 4);
        Pedido pedidoEncomienda = new PedidoEncomienda("002", "Av. Independencia 123", 6);
        Pedido pedidoExpress = new PedidoExpress("003", "Av. Apoquindo 1500", 7);

        mostrarTiempoEstimado(pedidoComida);
        mostrarTiempoEstimado(pedidoEncomienda);
        mostrarTiempoEstimado(pedidoExpress);
    }

    private static void mostrarTiempoEstimado(Pedido pedido) {
        pedido.mostrarResumen();
        System.out.println("Tiempo estimado de entrega: "
                + pedido.calcularTiempoEntrega() + " minutos\n");
    }
}
