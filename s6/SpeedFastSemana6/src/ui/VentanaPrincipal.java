package ui;

import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GestorPedidos;
import model.Pedido;
import model.Repartidor;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private final GestorPedidos gestorPedidos = new GestorPedidos();
    private final VentanaListaPedidos ventanaLista = new VentanaListaPedidos(gestorPedidos);
    private final Repartidor[] repartidores = {
        new Repartidor("Camila"),
        new Repartidor("Juan"),
        new Repartidor("Pedro")
    };

    public VentanaPrincipal() {
        setTitle("SpeedFast - Gestión de pedidos");
        setSize(360, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton botonRegistrar = new JButton("Registrar pedido");
        JButton botonListar = new JButton("Listar pedidos");
        JButton botonAsignar = new JButton("Asignar repartidor / Iniciar entrega");

        botonRegistrar.addActionListener(evento -> abrirRegistro());
        botonListar.addActionListener(evento -> abrirLista());
        botonAsignar.addActionListener(evento -> asignarRepartidor());

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBotones.add(botonRegistrar);
        panelBotones.add(botonListar);
        panelBotones.add(botonAsignar);
        add(panelBotones);
    }

    private void abrirRegistro() {
        new VentanaRegistroPedido(gestorPedidos, ventanaLista::actualizarTabla).setVisible(true);
    }

    private void abrirLista() {
        ventanaLista.actualizarTabla();
        ventanaLista.setVisible(true);
    }

    private void asignarRepartidor() {
        List<Pedido> pendientes = gestorPedidos.obtenerPedidosPendientes();
        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes para asignar.");
            return;
        }

        Pedido pedido = (Pedido) JOptionPane.showInputDialog(this,
                "Seleccione el pedido:", "Asignar repartidor",
                JOptionPane.QUESTION_MESSAGE, null, pendientes.toArray(), pendientes.get(0));
        if (pedido == null) {
            return;
        }

        Repartidor repartidor = (Repartidor) JOptionPane.showInputDialog(this,
                "Seleccione el repartidor:", "Iniciar entrega",
                JOptionPane.QUESTION_MESSAGE, null, repartidores, repartidores[0]);
        if (repartidor == null) {
            return;
        }

        gestorPedidos.iniciarEntrega(pedido, repartidor);
        ventanaLista.actualizarTabla();
        JOptionPane.showMessageDialog(this, "Entrega iniciada con " + repartidor.getNombre() + ".");
    }
}
