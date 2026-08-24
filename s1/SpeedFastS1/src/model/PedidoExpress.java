package model;

public class PedidoExpress extends Pedido {
    private static final int TIEMPO_BASE = 10;
    private static final int DISTANCIA_LIMITE = 5;
    private static final int TIEMPO_EXTRA = 5;

    public PedidoExpress(String idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, "Express", distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        if (getDistanciaKm() > DISTANCIA_LIMITE) {
            return TIEMPO_BASE + TIEMPO_EXTRA;
        }

        return TIEMPO_BASE;
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Express]");
        System.out.println("Asignando repartidor...");
        System.out.println("Repartidor más cercano con disponibilidad inmediata encontrado.");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        asignarRepartidor();
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}
