package ui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.PedidoDAO;
import model.Pedido;

public class VentanaListaPedidos extends JFrame {
    private static final long serialVersionUID = 1L;
    private final PedidoDAO pedidoDAO;
    private final DefaultTableModel modeloTabla;

    public VentanaListaPedidos(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
        setTitle("Pedidos registrados");
        setSize(700, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Dirección", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        JTable tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setFillsViewportHeight(true);
        JButton botonActualizar = new JButton("Actualizar lista");
        botonActualizar.addActionListener(evento -> actualizarTabla());

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);
        panel.add(botonActualizar, BorderLayout.SOUTH);
        add(panel);
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);

        try {
            for (Pedido pedido : pedidoDAO.listarTodos()) {
                modeloTabla.addRow(new Object[]{pedido.getId(), pedido.getDireccion(),
                    pedido.getTipo(), pedido.getEstado()});
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "No fue posible consultar los pedidos: "
                    + exception.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
}
