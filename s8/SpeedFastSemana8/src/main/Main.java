package main;

import javax.swing.SwingUtilities;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import ui.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal(
                new PedidoDAO(), new RepartidorDAO(), new EntregaDAO()).setVisible(true));
    }
}
