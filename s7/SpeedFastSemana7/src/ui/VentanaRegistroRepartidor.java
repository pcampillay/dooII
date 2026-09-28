package ui;

import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.RepartidorDAO;
import model.Repartidor;

public class VentanaRegistroRepartidor extends JFrame {
    private static final long serialVersionUID = 1L;
    private final RepartidorDAO repartidorDAO;
    private final JTextField campoNombre = new JTextField();

    public VentanaRegistroRepartidor(RepartidorDAO repartidorDAO) {
        this.repartidorDAO = repartidorDAO;
        setTitle("Registrar repartidor");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombre);

        JButton botonGuardar = new JButton("Guardar repartidor");
        botonGuardar.addActionListener(evento -> guardarRepartidor());
        panel.add(new JLabel());
        panel.add(botonGuardar);
        add(panel);
    }

    private void guardarRepartidor() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del repartidor.",
                    "Dato incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            repartidorDAO.guardar(new Repartidor(nombre));
            campoNombre.setText("");
            JOptionPane.showMessageDialog(this, "Repartidor guardado en la base de datos.");
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "No fue posible guardar el repartidor: "
                    + exception.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
