package app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.Pedido;
import model.Repartidor;
import model.ZonaDeCarga;

public class Main {

    public static void main(String[] args) {
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
        System.out.println("[Zona de carga inicializada]\n");

        agregarPedidos(zonaDeCarga);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(new Repartidor("Juan", zonaDeCarga));
        executor.execute(new Repartidor("Camila", zonaDeCarga));
        executor.execute(new Repartidor("Pedro", zonaDeCarga));
        executor.shutdown();

        esperarFinalizacion(executor);
        System.out.println("\n[Zona de carga vacía]");
        System.out.println("Todos los pedidos han sido entregados correctamente.");
    }

    private static void agregarPedidos(ZonaDeCarga zonaDeCarga) {
        zonaDeCarga.agregarPedido(new Pedido(1, "Santiago Centro"));
        zonaDeCarga.agregarPedido(new Pedido(2, "Providencia"));
        zonaDeCarga.agregarPedido(new Pedido(3, "Ñuñoa"));
        zonaDeCarga.agregarPedido(new Pedido(4, "Recoleta"));
        zonaDeCarga.agregarPedido(new Pedido(5, "Las Condes"));
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
