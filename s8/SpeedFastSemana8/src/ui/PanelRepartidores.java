package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.RepartidorDAO;
import model.Repartidor;

public class PanelRepartidores extends JPanel {
    private static final long serialVersionUID = 1L;
    private final RepartidorDAO repartidorDAO;
    private final Runnable alCambiarDatos;
    private final JTextField campoNombre = new JTextField();
    private final JTable tabla;
    private final JPanel panelEdicion;
    private final JSplitPane division;
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Nombre"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private Integer idSeleccionado;

    public PanelRepartidores(RepartidorDAO repartidorDAO, Runnable alCambiarDatos) {
        this.repartidorDAO = repartidorDAO;
        this.alCambiarDatos = alCambiarDatos;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarSeleccion(tabla.getSelectedRow());
            }
        });
        panelEdicion = crearPanelEdicion();
        JButton nuevo = new JButton("Nuevo repartidor");
        nuevo.addActionListener(evento -> abrirNuevo());
        division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tabla), null);
        division.setResizeWeight(1.0);
        division.setDividerSize(0);
        add(nuevo, BorderLayout.NORTH);
        add(division, BorderLayout.CENTER);
        actualizarTabla();
    }

    private JPanel crearPanelEdicion() {
        JPanel formulario = new JPanel(new GridLayout(0, 1, 0, 5));
        formulario.add(new JLabel("Nombre del repartidor"));
        formulario.add(campoNombre);

        JButton guardar = new JButton("Guardar");
        JButton eliminar = new JButton("Eliminar");
        JButton cancelar = new JButton("Cancelar");
        JButton cerrar = new JButton("×");
        cerrar.setToolTipText("Cerrar formulario");
        guardar.addActionListener(evento -> guardar());
        eliminar.addActionListener(evento -> eliminar());
        cancelar.addActionListener(evento -> cerrarFormulario());
        cerrar.addActionListener(evento -> cerrarFormulario());
        JPanel botones = new JPanel(new GridLayout(0, 1, 0, 6));
        botones.add(guardar);
        botones.add(eliminar);
        botones.add(cancelar);

        JPanel titulo = new JPanel(new BorderLayout());
        titulo.add(new JLabel("Datos del repartidor"), BorderLayout.CENTER);
        titulo.add(cerrar, BorderLayout.EAST);
        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        contenido.setPreferredSize(new Dimension(340, 180));
        contenido.add(titulo, BorderLayout.NORTH);
        contenido.add(formulario, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);
        return contenido;
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (Repartidor repartidor : repartidorDAO.readAll()) {
                modeloTabla.addRow(new Object[]{repartidor.getId(), repartidor.getNombre()});
            }
        } catch (SQLException exception) {
            mostrarError("consultar repartidores", exception);
        }
    }

    private void cargarSeleccion(int fila) {
        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        campoNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        mostrarFormulario();
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarValidacion("Ingrese el nombre del repartidor.");
            return;
        }
        Repartidor repartidor = new Repartidor(idSeleccionado, nombre);
        try {
            if (idSeleccionado == null) {
                repartidorDAO.create(repartidor);
            } else {
                repartidorDAO.update(repartidor);
            }
            cerrarFormulario();
            alCambiarDatos.run();
            JOptionPane.showMessageDialog(this, "Repartidor guardado correctamente.");
        } catch (SQLException exception) {
            mostrarError("guardar el repartidor", exception);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarValidacion("Seleccione un repartidor de la tabla.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el repartidor seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            repartidorDAO.delete(idSeleccionado);
            cerrarFormulario();
            alCambiarDatos.run();
            JOptionPane.showMessageDialog(this, "Repartidor eliminado correctamente.");
        } catch (SQLException exception) {
            mostrarError("eliminar el repartidor. Compruebe que no tenga entregas asociadas", exception);
        }
    }

    private void abrirNuevo() {
        idSeleccionado = null;
        campoNombre.setText("");
        tabla.clearSelection();
        mostrarFormulario();
    }

    private void mostrarFormulario() {
        if (division.getRightComponent() == null) {
            division.setRightComponent(panelEdicion);
            division.setDividerSize(8);
        }
        division.setResizeWeight(0.62);
        division.setDividerLocation(0.62);
        revalidate();
        repaint();
    }

    private void cerrarFormulario() {
        idSeleccionado = null;
        campoNombre.setText("");
        tabla.clearSelection();
        division.setRightComponent(null);
        division.setDividerSize(0);
        division.setResizeWeight(1.0);
        revalidate();
        repaint();
    }

    private void mostrarValidacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Dato requerido", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(String accion, SQLException exception) {
        JOptionPane.showMessageDialog(this, "No fue posible " + accion + ": " + exception.getMessage(),
                "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}
