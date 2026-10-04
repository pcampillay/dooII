package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
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

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

public class PanelEntregas extends JPanel {
    private static final long serialVersionUID = 1L;
    private final EntregaDAO entregaDAO;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final Runnable alCambiarDatos;
    private final JComboBox<Pedido> selectorPedido = new JComboBox<>();
    private final JComboBox<Repartidor> selectorRepartidor = new JComboBox<>();
    private final JComboBox<String> filtroPedido = new JComboBox<>();
    private final JComboBox<String> filtroRepartidor = new JComboBox<>();
    private final JTextField campoFecha = new JTextField(LocalDate.now().toString());
    private final JTextField campoHora = new JTextField(LocalTime.now().withNano(0).toString());
    private final JTable tabla;
    private final JPanel panelEdicion;
    private final JSplitPane division;
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Pedido", "Dirección", "ID repartidor", "Repartidor", "Fecha", "Hora"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private Integer idSeleccionado;
    private boolean actualizandoFiltros;

    public PanelEntregas(EntregaDAO entregaDAO, PedidoDAO pedidoDAO,
            RepartidorDAO repartidorDAO, Runnable alCambiarDatos) {
        this.entregaDAO = entregaDAO;
        this.pedidoDAO = pedidoDAO;
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
        JButton nueva = new JButton("Nueva entrega");
        nueva.addActionListener(evento -> abrirNuevo());
        JPanel barraSuperior = new JPanel(new BorderLayout(8, 8));
        barraSuperior.add(crearFiltros(), BorderLayout.CENTER);
        barraSuperior.add(nueva, BorderLayout.EAST);
        division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tabla), null);
        division.setResizeWeight(1.0);
        division.setDividerSize(0);
        add(barraSuperior, BorderLayout.NORTH);
        add(division, BorderLayout.CENTER);
        filtroPedido.addActionListener(evento -> actualizarTabla());
        filtroRepartidor.addActionListener(evento -> actualizarTabla());
        actualizarDatos();
    }

    private JPanel crearFiltros() {
        JPanel filtros = new JPanel(new GridLayout(1, 4, 8, 0));
        filtros.add(new JLabel("Filtrar por pedido"));
        filtros.add(filtroPedido);
        filtros.add(new JLabel("Filtrar por repartidor"));
        filtros.add(filtroRepartidor);
        return filtros;
    }

    private JPanel crearPanelEdicion() {
        JPanel formulario = new JPanel(new GridLayout(0, 1, 0, 5));
        formulario.add(new JLabel("Pedido"));
        formulario.add(selectorPedido);
        formulario.add(new JLabel("Repartidor"));
        formulario.add(selectorRepartidor);
        formulario.add(new JLabel("Fecha (AAAA-MM-DD)"));
        formulario.add(campoFecha);
        formulario.add(new JLabel("Hora (HH:MM:SS)"));
        formulario.add(campoHora);

        JButton guardar = new JButton("Guardar");
        JButton eliminar = new JButton("Eliminar");
        JButton cancelar = new JButton("Cancelar");
        JButton cerrar = new JButton("×");
        cerrar.setToolTipText("Cerrar formulario");
        guardar.addActionListener(evento -> guardar());
        eliminar.addActionListener(evento -> eliminar());
        cancelar.addActionListener(evento -> cerrarFormulario());
        cerrar.addActionListener(evento -> cerrarFormulario());
        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        botones.add(guardar);
        botones.add(eliminar);
        botones.add(cancelar);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        contenido.setPreferredSize(new Dimension(390, 340));
        JPanel titulo = new JPanel(new BorderLayout());
        titulo.add(new JLabel("Datos de la entrega"), BorderLayout.CENTER);
        titulo.add(cerrar, BorderLayout.EAST);
        contenido.add(titulo, BorderLayout.NORTH);
        contenido.add(formulario, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);
        return contenido;
    }

    public void actualizarDatos() {
        actualizandoFiltros = true;
        try {
            recargarSelectores();
            actualizarTabla();
        } catch (SQLException exception) {
            mostrarError("cargar pedidos y repartidores", exception);
        } finally {
            actualizandoFiltros = false;
        }
        actualizarTabla();
    }

    private void recargarSelectores() throws SQLException {
        Integer pedidoActual = idPedidoSeleccionado();
        Integer repartidorActual = idRepartidorSeleccionado();
        selectorPedido.removeAllItems();
        selectorRepartidor.removeAllItems();
        filtroPedido.removeAllItems();
        filtroRepartidor.removeAllItems();
        filtroPedido.addItem("Todos");
        filtroRepartidor.addItem("Todos");

        List<Pedido> pedidos = pedidoDAO.readAll();
        for (Pedido pedido : pedidos) {
            selectorPedido.addItem(pedido);
            filtroPedido.addItem(pedido.getId() + " - " + pedido.getDireccion());
        }
        List<Repartidor> repartidores = repartidorDAO.readAll();
        for (Repartidor repartidor : repartidores) {
            selectorRepartidor.addItem(repartidor);
            filtroRepartidor.addItem(repartidor.getId() + " - " + repartidor.getNombre());
        }
        seleccionarPedidoPorId(pedidoActual);
        seleccionarRepartidorPorId(repartidorActual);
    }

    public void actualizarTabla() {
        if (actualizandoFiltros) {
            return;
        }
        modeloTabla.setRowCount(0);
        try {
            for (Entrega entrega : entregaDAO.readAll(idDesdeFiltro(filtroPedido),
                    idDesdeFiltro(filtroRepartidor))) {
                modeloTabla.addRow(new Object[]{entrega.getId(), entrega.getPedido().getId(),
                    entrega.getPedido().getDireccion(), entrega.getRepartidor().getId(),
                    entrega.getRepartidor().getNombre(), entrega.getFecha(), entrega.getHora()});
            }
        } catch (SQLException exception) {
            mostrarError("consultar entregas", exception);
        }
    }

    private void cargarSeleccion(int fila) {
        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        Integer idPedido = (Integer) modeloTabla.getValueAt(fila, 1);
        Integer idRepartidor = (Integer) modeloTabla.getValueAt(fila, 3);
        seleccionarPedidoPorId(idPedido);
        seleccionarRepartidorPorId(idRepartidor);
        campoFecha.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
        campoHora.setText(String.valueOf(modeloTabla.getValueAt(fila, 6)));
        mostrarFormulario();
    }

    private void guardar() {
        Pedido pedido = (Pedido) selectorPedido.getSelectedItem();
        Repartidor repartidor = (Repartidor) selectorRepartidor.getSelectedItem();
        if (pedido == null || repartidor == null) {
            mostrarValidacion("Seleccione un pedido y un repartidor.");
            return;
        }

        LocalDate fecha;
        LocalTime hora;
        try {
            fecha = LocalDate.parse(campoFecha.getText().trim());
            hora = LocalTime.parse(campoHora.getText().trim());
        } catch (DateTimeParseException exception) {
            mostrarValidacion("Revise la fecha y la hora. Use AAAA-MM-DD y HH:MM.");
            return;
        }

        Entrega entrega = new Entrega(idSeleccionado, pedido, repartidor, fecha, hora);
        try {
            if (idSeleccionado == null) {
                entregaDAO.create(entrega);
            } else {
                entregaDAO.update(entrega);
            }
            cerrarFormulario();
            alCambiarDatos.run();
            JOptionPane.showMessageDialog(this, "Entrega guardada correctamente.");
        } catch (SQLException exception) {
            mostrarError("guardar la entrega", exception);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            mostrarValidacion("Seleccione una entrega de la tabla.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar la entrega seleccionada?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            entregaDAO.delete(idSeleccionado);
            cerrarFormulario();
            alCambiarDatos.run();
            JOptionPane.showMessageDialog(this, "Entrega eliminada correctamente.");
        } catch (SQLException exception) {
            mostrarError("eliminar la entrega", exception);
        }
    }

    private Integer idPedidoSeleccionado() {
        Pedido pedido = (Pedido) selectorPedido.getSelectedItem();
        return pedido == null ? null : pedido.getId();
    }

    private Integer idRepartidorSeleccionado() {
        Repartidor repartidor = (Repartidor) selectorRepartidor.getSelectedItem();
        return repartidor == null ? null : repartidor.getId();
    }

    private Integer idDesdeFiltro(JComboBox<String> filtro) {
        String seleccionado = (String) filtro.getSelectedItem();
        if (seleccionado == null || "Todos".equals(seleccionado)) {
            return null;
        }
        return Integer.valueOf(seleccionado.substring(0, seleccionado.indexOf(" - ")));
    }

    private void seleccionarPedidoPorId(Integer id) {
        for (int indice = 0; indice < selectorPedido.getItemCount(); indice++) {
            if (selectorPedido.getItemAt(indice).getId().equals(id)) {
                selectorPedido.setSelectedIndex(indice);
                return;
            }
        }
    }

    private void seleccionarRepartidorPorId(Integer id) {
        for (int indice = 0; indice < selectorRepartidor.getItemCount(); indice++) {
            if (selectorRepartidor.getItemAt(indice).getId().equals(id)) {
                selectorRepartidor.setSelectedIndex(indice);
                return;
            }
        }
    }

    private void abrirNuevo() {
        idSeleccionado = null;
        campoFecha.setText(LocalDate.now().toString());
        campoHora.setText(LocalTime.now().withNano(0).toString());
        tabla.clearSelection();
        if (selectorPedido.getItemCount() > 0) {
            selectorPedido.setSelectedIndex(0);
        }
        if (selectorRepartidor.getItemCount() > 0) {
            selectorRepartidor.setSelectedIndex(0);
        }
        mostrarFormulario();
    }

    private void mostrarFormulario() {
        if (division.getRightComponent() == null) {
            division.setRightComponent(panelEdicion);
            division.setDividerSize(8);
        }
        division.setResizeWeight(0.58);
        division.setDividerLocation(0.58);
        revalidate();
        repaint();
    }

    private void cerrarFormulario() {
        idSeleccionado = null;
        campoFecha.setText(LocalDate.now().toString());
        campoHora.setText(LocalTime.now().withNano(0).toString());
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
