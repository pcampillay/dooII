package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import model.Entrega;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;
import model.TipoPedido;

public class EntregaDAO {
    public void create(Entrega entrega) throws SQLException {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignarParametros(sentencia, entrega);
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    entrega.setId(claves.getInt(1));
                }
            }
        }
    }

    public List<Entrega> readAll() throws SQLException {
        return readAll(null, null);
    }

    public List<Entrega> readAll(Integer idPedido, Integer idRepartidor) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT e.id, e.fecha, e.hora, "
                + "p.id AS pedido_id, p.direccion, p.tipo, p.estado, "
                + "r.id AS repartidor_id, r.nombre "
                + "FROM entregas e JOIN pedidos p ON p.id = e.id_pedido "
                + "JOIN repartidores r ON r.id = e.id_repartidor WHERE 1 = 1");
        if (idPedido != null) {
            sql.append(" AND p.id = ?");
        }
        if (idRepartidor != null) {
            sql.append(" AND r.id = ?");
        }
        sql.append(" ORDER BY e.id");
        List<Entrega> entregas = new ArrayList<>();

        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql.toString())) {
            int parametro = 1;
            if (idPedido != null) {
                sentencia.setInt(parametro++, idPedido);
            }
            if (idRepartidor != null) {
                sentencia.setInt(parametro, idRepartidor);
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    Pedido pedido = new Pedido(resultado.getInt("pedido_id"), resultado.getString("direccion"),
                            TipoPedido.valueOf(resultado.getString("tipo")),
                            EstadoPedido.valueOf(resultado.getString("estado")));
                    Repartidor repartidor = new Repartidor(resultado.getInt("repartidor_id"),
                            resultado.getString("nombre"));
                    entregas.add(new Entrega(resultado.getInt("id"), pedido, repartidor,
                            resultado.getDate("fecha").toLocalDate(), resultado.getTime("hora").toLocalTime()));
                }
            }
        }
        return entregas;
    }

    public void update(Entrega entrega) throws SQLException {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignarParametros(sentencia, entrega);
            sentencia.setInt(5, entrega.getId());
            sentencia.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM entregas WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }

    private void asignarParametros(PreparedStatement sentencia, Entrega entrega) throws SQLException {
        sentencia.setInt(1, entrega.getPedido().getId());
        sentencia.setInt(2, entrega.getRepartidor().getId());
        sentencia.setDate(3, Date.valueOf(entrega.getFecha()));
        sentencia.setTime(4, Time.valueOf(entrega.getHora()));
    }
}
