package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.GestorPedidos;
import model.Pedido;

public class VentanaListaPedidos extends JFrame {
    private static final long serialVersionUID = 1L;
    private final GestorPedidos gestorPedidos;
    private final DefaultTableModel modeloTabla;

    public VentanaListaPedidos(GestorPedidos gestorPedidos) {
        this.gestorPedidos = gestorPedidos;
        setTitle("Pedidos registrados");
        setSize(new Dimension(700, 300));
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Dirección", "Tipo", "Estado", "Repartidor"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        JTable tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setFillsViewportHeight(true);
        JButton botonActualizar = new JButton("Actualizar lista");
        botonActualizar.addActionListener(evento -> actualizarTabla());

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 12));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);
        panelPrincipal.add(botonActualizar, BorderLayout.SOUTH);
        add(panelPrincipal);
        actualizarTabla();
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos = gestorPedidos.obtenerPedidos();

        for (Pedido pedido : pedidos) {
            String repartidor = pedido.getRepartidor() == null
                    ? "Sin asignar" : pedido.getRepartidor().getNombre();
            modeloTabla.addRow(new Object[]{
                pedido.getId(),
                pedido.getDireccionEntrega(),
                pedido.getTipo(),
                pedido.getEstado(),
                repartidor
            });
        }
    }
}
