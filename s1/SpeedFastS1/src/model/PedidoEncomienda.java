package model;

public class PedidoEncomienda extends Pedido {
    private static final int TIEMPO_BASE = 20;
    private static final double MINUTOS_POR_KM = 1.5;

    public PedidoEncomienda(String idPedido, String direccionEntrega, int distanciaKm) {
        super(idPedido, direccionEntrega, "Encomienda", distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(TIEMPO_BASE + MINUTOS_POR_KM * getDistanciaKm());
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Encomienda]");
        System.out.println("Asignando repartidor...");
        System.out.println("Validando peso y embalaje... OK");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        asignarRepartidor();
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}
