package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.EstadoPedido;
import model.Pedido;
import model.TipoPedido;

public class PedidoDAO {
    public void guardar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, pedido.getDireccion());
            sentencia.setString(2, pedido.getTipo().name());
            sentencia.setString(3, pedido.getEstado().name());
            sentencia.executeUpdate();
            asignarIdGenerado(pedido, sentencia);
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        return listarPorEstado(null);
    }

    public List<Pedido> listarPendientes() throws SQLException {
        return listarPorEstado(EstadoPedido.PENDIENTE);
    }

    public void actualizarEstado(Pedido pedido, EstadoPedido estado) throws SQLException {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, estado.name());
            sentencia.setInt(2, pedido.getId());
            sentencia.executeUpdate();
            pedido.setEstado(estado);
        }
    }

    private List<Pedido> listarPorEstado(EstadoPedido estado) throws SQLException {
        String sql = estado == null ? "SELECT * FROM pedido ORDER BY id"
                : "SELECT * FROM pedido WHERE estado = ? ORDER BY id";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (estado != null) {
                sentencia.setString(1, estado.name());
            }

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    pedidos.add(crearPedido(resultado));
                }
            }
        }

        return pedidos;
    }

    private void asignarIdGenerado(Pedido pedido, PreparedStatement sentencia) throws SQLException {
        try (ResultSet claves = sentencia.getGeneratedKeys()) {
            if (claves.next()) {
                pedido.setId(claves.getInt(1));
            }
        }
    }

    private Pedido crearPedido(ResultSet resultado) throws SQLException {
        return new Pedido(resultado.getInt("id"), resultado.getString("direccion"),
                TipoPedido.valueOf(resultado.getString("tipo")),
                EstadoPedido.valueOf(resultado.getString("estado")));
    }
}
