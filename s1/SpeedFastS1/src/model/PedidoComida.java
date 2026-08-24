package model;

public class PedidoComida extends Pedido {
    private static final int TIEMPO_BASE = 15;
    private static final int MINUTOS_POR_KM = 2;

    public PedidoComida(String idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, "Comida", distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        return TIEMPO_BASE + MINUTOS_POR_KM * getDistanciaKm();
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Comida]");
        System.out.println("Asignando repartidor...");
        System.out.println("Verificando mochila térmica... OK");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        asignarRepartidor();
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}
