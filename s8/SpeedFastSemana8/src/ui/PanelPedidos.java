package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.PedidoDAO;
import model.EstadoPedido;
import model.Pedido;
import model.TipoPedido;

public class PanelPedidos extends JPanel {
    private static final long serialVersionUID = 1L;
    private final PedidoDAO pedidoDAO;
    private final Runnable alCambiarDatos;
    private final JTextField campoDireccion = new JTextField();
    private final JComboBox<TipoPedido> selectorTipo = new JComboBox<>(TipoPedido.values());
    private final JComboBox<EstadoPedido> selectorEstado = new JComboBox<>(EstadoPedido.values());
    private final JComboBox<String> filtroEstado = new JComboBox<>(new String[]{
            "Todos", "PENDIENTE", "EN_REPARTO", "ENTREGADO"});
    private final JComboBox<String> filtroTipo = new JComboBox<>(new String[]{
            "Todos", "COMIDA", "ENCOMIENDA", "EXPRESS"});
    private final JTable tabla;
    private final JPanel panelEdicion;
    private final JSplitPane division;
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Dirección", "Tipo", "Estado"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private Integer idSeleccionado;

    public PanelPedidos(PedidoDAO pedidoDAO, Runnable alCambiarDatos) {
        this.pedidoDAO = pedidoDAO;
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
        JButton nuevo = new JButton("Nuevo pedido");
        nuevo.addActionListener(evento -> abrirNuevo());
        JPanel barraSuperior = new JPanel(new BorderLayout(8, 8));
        barraSuperior.add(crearPanelFiltros(), BorderLayout.CENTER);
        barraSuperior.add(nuevo, BorderLayout.EAST);
        division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tabla), null);
        division.setResizeWeight(1.0);
        division.setDividerSize(0);
        add(barraSuperior, BorderLayout.NORTH);
        add(division, BorderLayout.CENTER);
        filtroEstado.addActionListener(evento -> actualizarTabla());
        filtroTipo.addActionListener(evento -> actualizarTabla());
        actualizarTabla();
    }

    private JPanel crearPanelEdicion() {
        JPanel formulario = new JPanel(new GridLayout(0, 1, 0, 5));
        formulario.add(new JLabel("Dirección"));
        formulario.add(campoDireccion);
        formulario.add(new JLabel("Tipo"));
        formulario.add(selectorTipo);
        formulario.add(new JLabel("Estado"));
        formulario.add(selectorEstado);

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

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        contenido.setPreferredSize(new Dimension(370, 260));
        JPanel titulo = new JPanel(new BorderLayout());
        titulo.add(new JLabel("Datos del pedido"), BorderLayout.CENTER);
        titulo.add(cerrar, BorderLayout.EAST);
        contenido.add(titulo, BorderLayout.NORTH);
        contenido.add(formulario, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);
        return contenido;
    }

    private JPanel crearPanelFiltros() {
        JPanel filtros = new JPanel(new GridLayout(1, 4, 8, 0));
        filtros.add(new JLabel("Filtrar por estado"));
        filtros.add(filtroEstado);
        filtros.add(new JLabel("Filtrar por tipo"));
        filtros.add(filtroTipo);
        return filtros;
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        try {
            EstadoPedido estado = filtroEstado.getSelectedIndex() == 0 ? null
                    : EstadoPedido.valueOf((String) filtroEstado.getSelectedItem());
            TipoPedido tipo = filtroTipo.getSelectedIndex() == 0 ? null
                    : TipoPedido.valueOf((String) filtroTipo.getSelectedItem());
            for (Pedido pedido : pedidoDAO.readAll(estado, tipo)) {
                modeloTabla.addRow(new Object[]{pedido.getId(), pedido.getDireccion(),
                    pedido.getTipo(), pedido.getEstado()});
            }
        } catch (SQLException exception) {
            mostrarError("consultar pedidos", exception);
        }
    }

    private void cargarSeleccion(int fila) {
        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        campoDireccion.setText((String) modeloTabla.getValueAt(fila, 1));
        selectorTipo.setSelectedItem(modeloTabla.getValueAt(fila, 2));
        selectorEstado.setSelectedItem(modeloTabla.getValueAt(fila, 3));
        mostrarFormulario();
    }

    private void guardar() {
        String direccion = campoDireccion.getText().trim();
        if (direccion.isEmpty()) {
            mostrarValidacion("Ingrese la dirección del pedido.");
            return;
        }
        Pedido pedido = new Pedido(idSeleccionado, direccion,
                (TipoPedido) selectorTipo.getSelectedItem(), (EstadoPedido) selectorEstado.getSelectedItem());
        try {
            if (idSeleccionado == null) {
                pedidoDAO.create(pedido);
            } else {
                pedidoDAO.update(pedido);
            }
            cerrarFormulario();
            alCambiarDatos.run();
            JOptionPane.showMessageDialog(this, "Pedido guardado correctamente.");
        } catch (SQLException exception) {
            mostrarError("guardar el pedido", exception);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarValidacion("Seleccione un pedido de la tabla.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el pedido seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            pedidoDAO.delete(idSeleccionado);
            cerrarFormulario();
            alCambiarDatos.run();
            JOptionPane.showMessageDialog(this, "Pedido eliminado correctamente.");
        } catch (SQLException exception) {
            mostrarError("eliminar el pedido. Compruebe que no tenga entregas asociadas", exception);
        }
    }

    private void abrirNuevo() {
        idSeleccionado = null;
        campoDireccion.setText("");
        selectorTipo.setSelectedIndex(0);
        selectorEstado.setSelectedItem(EstadoPedido.PENDIENTE);
        tabla.clearSelection();
        mostrarFormulario();
    }

    private void mostrarFormulario() {
        if (division.getRightComponent() == null) {
            division.setRightComponent(panelEdicion);
            division.setDividerSize(8);
        }
        division.setResizeWeight(0.60);
        division.setDividerLocation(0.60);
        revalidate();
        repaint();
    }

    private void cerrarFormulario() {
        idSeleccionado = null;
        campoDireccion.setText("");
        selectorTipo.setSelectedIndex(0);
        selectorEstado.setSelectedItem(EstadoPedido.PENDIENTE);
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
