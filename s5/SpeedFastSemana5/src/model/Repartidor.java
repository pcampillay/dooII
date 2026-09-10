package model;

public class Repartidor implements Runnable {
    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Pedido pedido = zonaDeCarga.retirarPedido();
            if (pedido == null) {
                return;
            }

            entregarPedido(pedido);
        }
    }

    private void entregarPedido(Pedido pedido) {
        System.out.println("[Repartidor - " + nombre + "] Retirando pedido #"
                + pedido.getId() + "...");
        pedido.setEstado(EstadoPedido.EN_REPARTO);
        System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());
        System.out.println("[Repartidor - " + nombre + "] Entregando pedido #"
                + pedido.getId() + "...");

        try {
            Thread.sleep(800);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("[Repartidor - " + nombre + "] Entrega interrumpida.");
            return;
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);
        System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());
    }
}
