package ui;

import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final EntregaDAO entregaDAO;
    private final VentanaListaPedidos ventanaLista;

    public VentanaPrincipal(PedidoDAO pedidoDAO, RepartidorDAO repartidorDAO, EntregaDAO entregaDAO) {
        this.pedidoDAO = pedidoDAO;
        this.repartidorDAO = repartidorDAO;
        this.entregaDAO = entregaDAO;
        ventanaLista = new VentanaListaPedidos(pedidoDAO);
        setTitle("SpeedFast - Gestión de pedidos");
        setSize(410, 290);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(crearPanelBotones());
    }

    private JPanel crearPanelBotones() {
        JButton botonPedido = new JButton("Registrar pedido");
        JButton botonRepartidor = new JButton("Registrar repartidor");
        JButton botonLista = new JButton("Listar pedidos");
        JButton botonEntrega = new JButton("Asignar repartidor / Iniciar entrega");

        botonPedido.addActionListener(evento -> abrirRegistroPedido());
        botonRepartidor.addActionListener(evento -> abrirRegistroRepartidor());
        botonLista.addActionListener(evento -> abrirLista());
        botonEntrega.addActionListener(evento -> iniciarEntrega());

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(botonPedido);
        panel.add(botonRepartidor);
        panel.add(botonLista);
        panel.add(botonEntrega);
        return panel;
    }

    private void abrirRegistroPedido() {
        new VentanaRegistroPedido(pedidoDAO, ventanaLista::actualizarTabla).setVisible(true);
    }

    private void abrirRegistroRepartidor() {
        new VentanaRegistroRepartidor(repartidorDAO).setVisible(true);
    }

    private void abrirLista() {
        ventanaLista.actualizarTabla();
        ventanaLista.setVisible(true);
    }

    private void iniciarEntrega() {
        try {
            Pedido pedido = seleccionarPedido();
            if (pedido == null) {
                return;
            }

            Repartidor repartidor = seleccionarRepartidor();
            if (repartidor == null) {
                return;
            }

            entregaDAO.guardar(new Entrega(pedido, repartidor, LocalDate.now(), LocalTime.now()));
            pedidoDAO.actualizarEstado(pedido, EstadoPedido.EN_REPARTO);
            ventanaLista.actualizarTabla();
            JOptionPane.showMessageDialog(this, "Entrega registrada correctamente.");
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "No fue posible iniciar la entrega: "
                    + exception.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Pedido seleccionarPedido() throws SQLException {
        List<Pedido> pedidos = pedidoDAO.listarPendientes();
        if (pedidos.isEmpty()) {
            mostrarSinRegistros();
            return null;
        }

        return (Pedido) JOptionPane.showInputDialog(this, "Seleccione el pedido:",
                "Iniciar entrega", JOptionPane.QUESTION_MESSAGE, null,
                pedidos.toArray(), pedidos.get(0));
    }

    private Repartidor seleccionarRepartidor() throws SQLException {
        List<Repartidor> repartidores = repartidorDAO.listarTodos();
        if (repartidores.isEmpty()) {
            mostrarSinRegistros();
            return null;
        }

        return (Repartidor) JOptionPane.showInputDialog(this, "Seleccione el repartidor:",
                "Iniciar entrega", JOptionPane.QUESTION_MESSAGE, null,
                repartidores.toArray(), repartidores.get(0));
    }

    private void mostrarSinRegistros() {
        JOptionPane.showMessageDialog(this, "No hay registros disponibles.");
    }
}
