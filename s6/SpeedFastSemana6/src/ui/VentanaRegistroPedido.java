package ui;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.GestorPedidos;
import model.Pedido;
import model.TipoPedido;

public class VentanaRegistroPedido extends JFrame {
    private static final long serialVersionUID = 1L;
    private final GestorPedidos gestorPedidos;
    private final Runnable alGuardar;
    private final JTextField campoId = new JTextField();
    private final JTextField campoDireccion = new JTextField();
    private final JComboBox<TipoPedido> selectorTipo = new JComboBox<>(TipoPedido.values());

    public VentanaRegistroPedido(GestorPedidos gestorPedidos, Runnable alGuardar) {
        this.gestorPedidos = gestorPedidos;
        this.alGuardar = alGuardar;
        setTitle("Registrar pedido");
        setSize(400, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("ID:"));
        panel.add(campoId);
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
        String textoId = campoId.getText().trim();
        String direccion = campoDireccion.getText().trim();

        if (textoId.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(textoId);
            if (id <= 0) {
                throw new NumberFormatException();
            }

            TipoPedido tipo = (TipoPedido) selectorTipo.getSelectedItem();
            gestorPedidos.agregarPedido(new Pedido(id, direccion, tipo));
            alGuardar.run();
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.");
            limpiarCampos();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número positivo.",
                    "ID inválido", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "No fue posible registrar", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        campoId.setText("");
        campoDireccion.setText("");
        selectorTipo.setSelectedIndex(0);
    }
}
