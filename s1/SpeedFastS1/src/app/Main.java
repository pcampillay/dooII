package app;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

public class Main {

    public static void main(String[] args) {
        Pedido pedidoComida = new PedidoComida("C001", "Av. Central 123");
        Pedido pedidoEncomienda = new PedidoEncomienda("E001", "Los Alerces 456");
        Pedido pedidoExpress = new PedidoExpress("X001", "Las Flores 789");

        probarAsignacion(pedidoComida, "Juan Pérez");
        probarAsignacion(pedidoEncomienda, "Camila Soto");
        probarAsignacion(pedidoExpress, "Luis Díaz");
    }

    private static void probarAsignacion(Pedido pedido, String nombreRepartidor) {
        System.out.println("Método sobrescrito:");
        pedido.asignarRepartidor();

        System.out.println("\nMétodo sobrecargado:");
        pedido.asignarRepartidor(nombreRepartidor);
        System.out.println("\n------------------------------\n");
    }
}
