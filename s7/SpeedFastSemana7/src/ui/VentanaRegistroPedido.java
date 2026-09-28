package ui;

import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.PedidoDAO;
import model.Pedido;
import model.TipoPedido;

public class VentanaRegistroPedido extends JFrame {
    private static final long serialVersionUID = 1L;
    private final PedidoDAO pedidoDAO;
    private final Runnable alGuardar;
    private final JTextField campoDireccion = new JTextField();
    private final JComboBox<TipoPedido> selectorTipo = new JComboBox<>(TipoPedido.values());

    public VentanaRegistroPedido(PedidoDAO pedidoDAO, Runnable alGuardar) {
        this.pedidoDAO = pedidoDAO;
        this.alGuardar = alGuardar;
        setTitle("Registrar pedido");
        setSize(400, 190);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Dirección:"));
        panel.add(campoDireccion);
        panel.add(new JLabel("Tipo:"));
        panel.add(selectorTipo);

        JButton botonGuardar = new JButton("Guardar pedido");
        botonGuardar.addActionListener(evento -> guardarPedido());
        panel.add(new JLabel());
        panel.add(botonGuardar);
        add(panel);
    }

    private void guardarPedido() {
        String direccion = campoDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la dirección del pedido.",
                    "Dato incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            pedidoDAO.guardar(new Pedido(direccion, (TipoPedido) selectorTipo.getSelectedItem()));
            alGuardar.run();
            campoDireccion.setText("");
            selectorTipo.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Pedido guardado en la base de datos.");
        } catch (SQLException exception) {
            mostrarError(exception);
        }
    }

    private void mostrarError(SQLException exception) {
        JOptionPane.showMessageDialog(this, "No fue posible guardar el pedido: "
                + exception.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}
