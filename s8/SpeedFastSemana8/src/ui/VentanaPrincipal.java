package ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private final PanelRepartidores panelRepartidores;
    private final PanelPedidos panelPedidos;
    private final PanelEntregas panelEntregas;

    public VentanaPrincipal(PedidoDAO pedidoDAO, RepartidorDAO repartidorDAO, EntregaDAO entregaDAO) {
        setTitle("SpeedFast - Gestión de pedidos");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelRepartidores = new PanelRepartidores(repartidorDAO, this::actualizarPaneles);
        panelPedidos = new PanelPedidos(pedidoDAO, this::actualizarPaneles);
        panelEntregas = new PanelEntregas(entregaDAO, pedidoDAO, repartidorDAO, this::actualizarPaneles);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Repartidores", panelRepartidores);
        pestanas.addTab("Pedidos", panelPedidos);
        pestanas.addTab("Entregas", panelEntregas);
        add(pestanas, BorderLayout.CENTER);
        actualizarPaneles();
    }

    private void actualizarPaneles() {
        panelRepartidores.actualizarTabla();
        panelPedidos.actualizarTabla();
        panelEntregas.actualizarDatos();
    }
}
