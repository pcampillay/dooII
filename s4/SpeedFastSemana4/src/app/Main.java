package app;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;

public class Main {

    public static void main(String[] args) {
        List<Pedido> pedidosCamila = Arrays.asList(
                new PedidoComida("101", "Av. Italia 456", 4),
                new PedidoExpress("102", "Av. Providencia 800", 3));
        List<Pedido> pedidosLuis = Arrays.asList(
                new PedidoEncomienda("103", "Av. Santa Rosa 567", 7),
                new PedidoComida("104", "Av. Vicuña Mackenna 1200", 5));
        List<Pedido> pedidosSofia = Arrays.asList(
                new PedidoExpress("105", "Av. Apoquindo 1500", 6),
                new PedidoEncomienda("106", "Av. Matta 300", 8));

        prepararPedidos("Camila", pedidosCamila);
        prepararPedidos("Luis", pedidosLuis);
        prepararPedidos("Sofía", pedidosSofia);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(new Repartidor("Camila", pedidosCamila));
        executor.execute(new Repartidor("Luis", pedidosLuis));
        executor.execute(new Repartidor("Sofía", pedidosSofia));
        executor.shutdown();

        esperarFinalizacion(executor);
        System.out.println("\nTodas las entregas fueron finalizadas.");
    }

    private static void prepararPedidos(String nombreRepartidor, List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {
            pedido.asignarRepartidor(nombreRepartidor);
            pedido.reservar();
        }
    }

    private static void esperarFinalizacion(ExecutorService executor) {
        try {
            while (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Esperando que finalicen las entregas pendientes...");
            }
        } catch (InterruptedException exception) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
