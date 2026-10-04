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
    public void create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, pedido.getDireccion());
            sentencia.setString(2, pedido.getTipo().name());
            sentencia.setString(3, pedido.getEstado().name());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    pedido.setId(claves.getInt(1));
                }
            }
        }
    }

    public List<Pedido> readAll() throws SQLException {
        return readAll(null, null);
    }

    public List<Pedido> readAll(EstadoPedido estado, TipoPedido tipo) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT id, direccion, tipo, estado FROM pedidos WHERE 1 = 1");
        if (estado != null) {
            sql.append(" AND estado = ?");
        }
        if (tipo != null) {
            sql.append(" AND tipo = ?");
        }
        sql.append(" ORDER BY id");
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql.toString())) {
            int parametro = 1;
            if (estado != null) {
                sentencia.setString(parametro++, estado.name());
            }
            if (tipo != null) {
                sentencia.setString(parametro, tipo.name());
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    pedidos.add(new Pedido(resultado.getInt("id"), resultado.getString("direccion"),
                            TipoPedido.valueOf(resultado.getString("tipo")),
                            EstadoPedido.valueOf(resultado.getString("estado"))));
                }
            }
        }
        return pedidos;
    }

    public void update(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, pedido.getDireccion());
            sentencia.setString(2, pedido.getTipo().name());
            sentencia.setString(3, pedido.getEstado().name());
            sentencia.setInt(4, pedido.getId());
            sentencia.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }
}
